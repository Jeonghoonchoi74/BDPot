package com.example.baedalpot.ui.home.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.baedalpot.R;
import com.example.baedalpot.adapter.ChatAdapter;
import com.example.baedalpot.databinding.FragmentGroupBinding;
import com.example.baedalpot.databinding.FragmentSerchBinding;
import com.example.baedalpot.model.Chat;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.model.User;
import com.example.baedalpot.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SerchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SerchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView tv_title;
    private TextView tv_member;
    private TextView tv_restaurant;
    private TextView tv_cash;
    private TextView tv_timer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentSerchBinding binding;
    private RecyclerView recyclerView;

    private final ChatAdapter adapter = new ChatAdapter(new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return TextUtils.equals(oldItem.getGroupKey(), newItem.getGroupKey());
        }
    });


    public SerchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SerchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SerchFragment newInstance(String param1, String param2) {
        SerchFragment fragment = new SerchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        View v = new View(this.getContext());
        recyclerView = v.findViewById(R.id.Chatrecycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSerchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}