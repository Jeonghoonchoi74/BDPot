package com.example.baedalpot.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baedalpot.databinding.ItemGroupBinding;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupAdapter extends ListAdapter<Group, GroupAdapter.GroupViewHolder> {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    public GroupAdapter() {
        super(new DiffUtil.ItemCallback<Group>() {
            @Override
            public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return TextUtils.equals(oldItem.key, newItem.key);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return TextUtils.equals(oldItem.title, newItem.title) &&
                        TextUtils.equals(oldItem.restaurant, newItem.restaurant) &&
                        TextUtils.equals(oldItem.category, newItem.category) &&
                        oldItem.maxPrice == newItem.maxPrice &&
                        oldItem.maxPerson == newItem.maxPerson &&
                        TextUtils.equals(oldItem.destination, newItem.destination);
            }

            @Override
            public Object getChangePayload(@NonNull Group oldItem, @NonNull Group newItem) {
                return new Object();
            }
        });
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGroupBinding binding = ItemGroupBinding.inflate(inflater, parent, false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        Group item = getItem(position);
        ItemGroupBinding binding = holder.binding;

        binding.titleTextView.setText(item.title);
        binding.restaurantTextView.setText("식당: " + item.restaurant);
        binding.categoryTextView.setText("카테고리: " + item.category);
        binding.maxPriceTextView.setText("최대금액: " + item.maxPrice);
        binding.maxPersonTextView.setText("최대인원수: " + item.maxPerson);
        binding.destinationTextView.setText("배달목적지: " + item.destination);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemGroupBinding binding;

        public GroupViewHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
