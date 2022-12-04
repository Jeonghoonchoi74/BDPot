package com.example.baedalpot.ui.home.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baedalpot.adapter.GroupAdapter;
import com.example.baedalpot.databinding.FragmentGroupBinding;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.ui.writepost.WritePostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupFragment extends Fragment implements ValueEventListener {
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FragmentGroupBinding binding;
    private final GroupAdapter adapter = new GroupAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
        binding.writeButton.setOnClickListener(v ->{
                Intent intent = new Intent(requireContext(), WritePostActivity.class);
                startActivity(intent);
        });

        db.getReference().child("Group")
                .orderByChild("timestampComplement")
                .addValueEventListener(this);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Group> groups = new ArrayList<>();

        for (DataSnapshot child : snapshot.getChildren()) {
            groups.add(child.getValue(Group.class));
        }

        adapter.submitList(groups);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        error.toException().printStackTrace();
    }
}