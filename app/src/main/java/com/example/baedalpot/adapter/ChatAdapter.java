package com.example.baedalpot.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baedalpot.R;
import com.example.baedalpot.databinding.FragmentSerchBinding;
import com.example.baedalpot.databinding.ItemChatBinding;
import com.example.baedalpot.databinding.ItemGroupBinding;
import com.example.baedalpot.model.Chat;
import com.example.baedalpot.model.Group;

import java.util.ArrayList;

public class ChatAdapter extends ListAdapter<Chat, ChatAdapter.ChatViewHolder> {

    public ChatAdapter(@NonNull DiffUtil.ItemCallback<Chat> diffCallback){
        super(diffCallback);
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChatBinding binding = ItemChatBinding.inflate(inflater, parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position).getMassage());
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder{
        ItemChatBinding binding;
        public ChatViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.tvMessage);
            this.binding = binding;
        }

        private void bind(String strNum) {
            binding.tvMessage.setText(strNum);
        }
    }

}
