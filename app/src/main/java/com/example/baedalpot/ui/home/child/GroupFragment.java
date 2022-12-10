package com.example.baedalpot.ui.home.child;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.baedalpot.R;
import com.example.baedalpot.adapter.GroupAdapter;
import com.example.baedalpot.databinding.FragmentGroupBinding;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.ui.writepost.WritePostActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupFragment extends Fragment implements ValueEventListener {
    private FragmentGroupBinding binding;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Query reference;

    private final MutableLiveData<String> category = new MutableLiveData<>("");
    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final MutableLiveData<List<Group>> groupList = new MutableLiveData<>(Collections.emptyList());
    private FilteredGroupList filteredGroupList;

    private final GroupAdapter adapter = new GroupAdapter(this);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = db.getReference().child("Group")
                .orderByChild("timestampComplement");
        filteredGroupList = new FilteredGroupList(category, query, groupList);

        AutoCompleteTextView categoryTextView = ((AutoCompleteTextView) binding.categoryTextField.getEditText());

        final String[] categories = getResources().getStringArray(R.array.Category);
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, categories);
        categoryTextView.setAdapter(dropdownAdapter);
        categoryTextView.setText(categories[0], false);
        categoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position == 0) {
                category.setValue("");
            } else {
                category.setValue(categories[position]);
            }
        });

        binding.searchTextField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                query.setValue(s.toString().trim());
            }
        });

        binding.searchTextField.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                binding.searchTextField.getEditText().clearFocus();
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }

            return false;
        });

        binding.recyclerView.setAdapter(adapter);
        filteredGroupList.observe(getViewLifecycleOwner(), groupList -> {
            adapter.submitList(groupList);
            binding.emptyView.setVisibility(!this.groupList.getValue().isEmpty() && groupList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        binding.writeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), WritePostActivity.class);
            startActivity(intent);
        });

        reference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Group> groups = new ArrayList<>();

        for (DataSnapshot child : snapshot.getChildren()) {
            groups.add(child.getValue(Group.class));
        }

        groupList.setValue(groups);
        binding.progressIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        error.toException().printStackTrace();
    }

    @Override
    public void onDestroy() {
        reference.removeEventListener(this);
        super.onDestroy();
    }

    private static class FilteredGroupList extends MediatorLiveData<List<Group>> {
        private String category = "";
        private String query = "";
        private List<Group> groupList = Collections.emptyList();

        private FilteredGroupList(LiveData<String> category, LiveData<String> query, LiveData<List<Group>> groupList) {
            addSource(category, s -> {
                this.category = s.trim();
                update();
            });

            addSource(query, s -> {
                this.query = s.trim();
                update();
            });

            addSource(groupList, list -> {
                this.groupList = list;
                update();
            });
        }

        private void update() {
            if (groupList.isEmpty()) {
                setValue(Collections.emptyList());
                return;
            }

            ArrayList<Group> filteredGroupList = new ArrayList<>();
            for (Group group : groupList) {
                if (!category.isEmpty()) {
                    if (!TextUtils.equals(category, group.category)) {
                        continue;
                    }
                }

                if (!query.isEmpty()) {
                    if (!group.title.contains(query)) {
                        continue;
                    }
                }

                filteredGroupList.add(group);
            }

            setValue(filteredGroupList);
        }
    }
}