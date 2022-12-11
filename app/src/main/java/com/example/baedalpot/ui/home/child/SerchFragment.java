package com.example.baedalpot.ui.home.child;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import android.text.TextUtils;
import android.util.Log;
import com.example.baedalpot.adapter.ChatAdapter;
import com.example.baedalpot.databinding.FragmentSerchBinding;
import com.example.baedalpot.model.Chat;
import com.example.baedalpot.model.Group;
import com.example.baedalpot.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SerchFragment extends Fragment implements ValueEventListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentSerchBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();




    private final ChatAdapter adapter = new ChatAdapter(new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return TextUtils.equals(oldItem.getMg(), newItem.getMg());
        }
    });


    public SerchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSerchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //information
        db.child("UserAccount").child(auth.getUid()).child("group").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String g = task.getResult().getValue().toString();
                db.child("Group").child(g).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Group gc = task.getResult().getValue(Group.class);
                        binding.tvTitle.setText(gc.getTitle());
                        binding.tvRestaurant.setText(gc.getRestaurant());
                        Integer i = gc.getMaxPrice();
                        binding.tvCash.setText("가용금액 : "+i.toString());
                        Integer size = gc.getUserlist().size();
                        Integer mx = gc.getMaxPerson();
                        binding.tvMember.setText("참가인원 : "+size.toString()+" / "+mx.toString());
                        binding.tvNumAgree.setText("동의인원: " + (gc.getNumAgree()));
                    }
                });
            }
        });

        //adapter
        binding.ChatrecyclerView.setAdapter(this.adapter);

        //exit
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("UserAccount").child(auth.getUid()).child("group").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String s = task.getResult().getValue(String.class);
                        db.child("Group").child(s).child("userlist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                ArrayList<Object> array = (ArrayList) task.getResult().getValue();
                                array.remove(auth.getUid());
                                db.child("Group").child(s).child("userlist").setValue(array);
                            }
                        });
                    }
                });
                db.child("UserAccount").child(auth.getUid()).child("group").setValue(null);
            }
        });


        //chat
        try{

            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = null;
                    try{
                        s = binding.etchat.getText().toString();
                        Log.e("ET",s);
                    }catch (NullPointerException e){
                        Log.d("log", "null");
                    }

                    Log.e("ET", "OK1");
                    String finalS = s;
                    db.child("UserAccount").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            User u = task.getResult().getValue(User.class);
                            Log.e("US", u.getId());
                            Log.e("US", u.getGroup());
                            Log.e("US", finalS);
                            Chat chat = new Chat(finalS, u.getId(), u.getGroup());
                            db.child("Group").child(u.getGroup()).child("Chat").push().setValue(chat);
                            db.child("Group").child(u.getGroup()).child("Chat").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Log.e("ET", "OK4");
                                        ArrayList<Chat> chats = new ArrayList<>();
                                        for(DataSnapshot s : snapshot.getChildren()){
                                            chats.add(s.getValue(Chat.class));
                                        }

                                        adapter.submitList(chats);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            binding.etchat.setText("");
                        }
                    });
                }
            });

        }catch(NullPointerException e) {
            Log.d("log", "null_button");
        }

        binding.radioButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("UserAccount").child(auth.getUid()).child("group").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String g = task.getResult().getValue().toString();
                        db.child("Group").child(g).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Group gc = task.getResult().getValue(Group.class);
                                db.child("Group").child(g).child("numAgree").setValue((gc.getNumAgree()+1));
                                binding.tvNumAgree.setText("동의인원: " + (gc.getNumAgree()+1));
                            }
                        });
                    }
                });
            }
        });

        binding.radioButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("UserAccount").child(auth.getUid()).child("group").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String g = task.getResult().getValue().toString();
                        db.child("Group").child(g).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Group gc = task.getResult().getValue(Group.class);
                                db.child("Group").child(g).child("numAgree").setValue((gc.getNumAgree()-1));
                                binding.tvNumAgree.setText("동의인원: " + (gc.getNumAgree()-1));
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Log.e("ET", "OK4");
        Log.e("data",snapshot.getKey());
        ArrayList<Chat> chats = new ArrayList<>();
        for(DataSnapshot s : snapshot.getChildren()){
            chats.add(new Chat(s.getValue(String.class), s.getKey(), "mg"));
        }

        adapter.submitList(chats);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        error.toException().printStackTrace();
    }
}