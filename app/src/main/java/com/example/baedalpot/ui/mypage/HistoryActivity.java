package com.example.baedalpot.ui.mypage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baedalpot.databinding.ActivityHistoryBinding;
import com.example.baedalpot.databinding.ItemHistoryChildBinding;
import com.example.baedalpot.databinding.ItemHistoryParentBinding;
import com.example.baedalpot.databinding.ItemHistoryProfileBinding;
import com.example.baedalpot.databinding.ItemHistoryRowBinding;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String uid;

    private Handler uiHandler;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uid = auth.getUid();
        if (uid == null) {
            finish();
            return;
        }

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uiHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what != 0) return;
                thread = null;
                if (isDestroyed()) return;

                List<GroupModel> groupList = (List<GroupModel>) msg.obj;
                binding.recyclerView.setAdapter(new GroupAdapter(groupList));
                binding.emptyView.setVisibility(groupList.isEmpty() ? View.VISIBLE : View.GONE);

                binding.progressIndicator.setVisibility(View.GONE);
            }
        };

        onInit();
        refresh();
    }

    private void onInit() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void refresh() {
        if (binding.progressIndicator.getVisibility() == View.VISIBLE) return;
        if (thread != null) return;

        binding.progressIndicator.setVisibility(View.VISIBLE);
        thread = new GroupGetter(uiHandler, uid);
        thread.start();
    }

    @IgnoreExtraProperties
    private static class GroupModel extends Group {
        @Exclude
        HashMap<String, User> profiles = new HashMap<>();

        @Exclude
        boolean isExpanded = false;
    }

    private static class GroupGetter extends Thread {
        private final Handler uiHandler;
        private final String uid;
        private final FirebaseDatabase db = FirebaseDatabase.getInstance();
        private final ArrayList<GroupModel> groupList = new ArrayList<>();

        private GroupGetter(Handler uiHandler, String uid) {
            this.uiHandler = uiHandler;
            this.uid = uid;
        }

        @Override
        public void run() {
            super.run();

            Log.d("GroupGetter", "start");

            try {
                DataSnapshot snapshot = Tasks.await(db.getReference().child("Group").get());
                HashSet<String> uidSet = new HashSet<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List<String> userList = child.child("userlist").getValue(t);
//                    if (userList != null) {
//                        Log.d("GroupGetter", "Group list count: " + Arrays.toString(userList.toArray()));
//                    }

                    if (userList != null && userList.contains(uid)) {
                        GroupModel group = child.getValue(GroupModel.class);

                        groupList.add(group);
                        uidSet.addAll(group.userlist);
                    }
                }

                Log.d("GroupGetter", "Group list count: " + groupList.size());
                Log.d("GroupGetter", "Uid list: " + Arrays.toString(uidSet.toArray()));

                List<Task<DataSnapshot>> tasks = uidSet.stream()
                        .map((uid) -> db.getReference()
                                .child("UserAccount")
                                .child(uid)
                                .get())
                        .collect(Collectors.toList());
                Tasks.await(Tasks.whenAllComplete(tasks));

                HashMap<String, User> profiles = new HashMap<>();
                for (Task<DataSnapshot> task : tasks) {
                    if (task.getException() != null) continue;

                    DataSnapshot element = task.getResult();
                    User user = element.getValue(User.class);
                    profiles.put(element.getKey(), user);
                }

                Log.d("GroupGetter", "Profiles size: " + profiles.size());

                for (GroupModel group : groupList) {
                    for (String uid : group.userlist) {
                        if (profiles.containsKey(uid)) {
                            group.profiles.put(uid, profiles.get(uid));
                        }
                    }
                }

                uiHandler.sendMessage(uiHandler.obtainMessage(0, groupList));

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ItemViewHolder> {
        private final List<GroupModel> groupList;

        private GroupAdapter(List<GroupModel> groupList) {
            this.groupList = groupList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemHistoryRowBinding binding = ItemHistoryRowBinding.inflate(inflater, parent, false);
            ((RecyclerView) binding.expandableLayout.secondLayout).setAdapter(new ProfileAdapter());

            return new ItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            GroupModel item = groupList.get(position);

            holder.binding.expandableLayout.parentLayout.setOnClickListener(v -> {
                item.isExpanded = !item.isExpanded;
                notifyItemChanged(position, new Object());
            });

            holder.parentBinding.getRoot().setText(item.title);
            ((ProfileAdapter) holder.childBinding.getRoot().getAdapter()).setItems(
                    new ArrayList<User>(item.profiles.values()));

            if (item.isExpanded) {
                holder.binding.expandableLayout.expand();
            } else {
                holder.binding.expandableLayout.collapse();
            }
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        static class ItemViewHolder extends RecyclerView.ViewHolder {
            final ItemHistoryRowBinding binding;
            final ItemHistoryParentBinding parentBinding;
            final ItemHistoryChildBinding childBinding;

            public ItemViewHolder(ItemHistoryRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                this.parentBinding = ItemHistoryParentBinding.bind(binding.expandableLayout.parentLayout);
                this.childBinding = ItemHistoryChildBinding.bind(binding.expandableLayout.secondLayout);
            }
        }
    }

    private static class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileItemViewHolder> {
        private final ArrayList<User> profiles = new ArrayList<>();

        public void setItems(List<User> profiles) {
            this.profiles.clear();
            this.profiles.addAll(profiles);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProfileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemHistoryProfileBinding binding = ItemHistoryProfileBinding.inflate(inflater, parent, false);
            return new ProfileItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileItemViewHolder holder, int position) {
            User item = profiles.get(position);
            holder.binding.idTextView.setText(item.id);
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        static class ProfileItemViewHolder extends RecyclerView.ViewHolder {
            final ItemHistoryProfileBinding binding;

            public ProfileItemViewHolder(ItemHistoryProfileBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}