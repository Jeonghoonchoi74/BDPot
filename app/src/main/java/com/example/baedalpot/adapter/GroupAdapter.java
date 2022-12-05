package com.example.baedalpot.adapter;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


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


        int p = position;
        holder.itemView.setTag(p);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getUid();
                ArrayList<String> array = getCurrentList().get(p).getUserlist();
                array.add(auth.getUid());
                db.child("Group").child("Group_"+getCurrentList().get(p).getKey()).child("userlist").setValue(array);
                db.child("UserAccount").child("Group").setValue("Group_"+getCurrentList().get(p).getKey());
            }
        });

    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemGroupBinding binding;

        public GroupViewHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}



















/*public class GroupAdapter extends  RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{
    private ArrayList<Group> arrayList;

    public GroupAdapter(ArrayList<Group> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        GroupViewHolder holder = new GroupViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_category.setText(arrayList.get(position).getTitle());
        holder.tv_reastaurant.setText(arrayList.get(position).getTitle());
        holder.tv_maxPrice.setText(arrayList.get(position).getTitle());
        holder.tv_maxPerson.setText(arrayList.get(position).getTitle());
        holder.tv_destination.setText(arrayList.get(position).getTitle());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_title;
        protected TextView tv_reastaurant;
        protected TextView tv_category;
        protected TextView tv_maxPrice;
        protected TextView tv_maxPerson;
        protected TextView tv_destination;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_category = (TextView) itemView.findViewById(R.id.tv_Group_category);
            this.tv_reastaurant = (TextView) itemView.findViewById(R.id.tv_Group_reastaurant);
            this.tv_maxPrice = (TextView) itemView.findViewById(R.id.tv_Group_maxPrice);
            this.tv_maxPerson = (TextView) itemView.findViewById(R.id.tv_Group_maxPerson);
            this.tv_destination = (TextView) itemView.findViewById(R.id.tv_Group_destination);
        }
    }
}*/
