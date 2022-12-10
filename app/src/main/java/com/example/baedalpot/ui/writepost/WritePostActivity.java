package com.example.baedalpot.ui.writepost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.example.baedalpot.BaseActivity;
import com.example.baedalpot.R;
import com.example.baedalpot.databinding.ActivityWritePostBinding;
import com.example.baedalpot.model.Chat;
import com.example.baedalpot.model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WritePostActivity extends BaseActivity {
    private ActivityWritePostBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private Integer g_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWritePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.maxPriceTextField.getEditText().setTransformationMethod(null);
        binding.maxPersonTextField.getEditText().setTransformationMethod(null);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.item_dropdown, getResources().getStringArray(R.array.Category));
        //((AutoCompleteTextView) binding.categoryTextField.getEditText()).setAdapter(adapter);

        binding.submitButton.setOnClickListener(v -> submit());
    }

    private void submit() {
        if (isProgressDialogVisible()) return;

        String title = binding.titleTextField.getEditText().getText().toString().trim();
        String restaurant = binding.restaurantTextField.getEditText().getText().toString().trim();
        String category = binding.categoryTextField.getEditText().getText().toString().trim();
        String maxPrice = binding.maxPriceTextField.getEditText().getText().toString().trim();
        String maxPerson = binding.maxPersonTextField.getEditText().getText().toString().trim();
        String destination = binding.destinationTextField.getEditText().getText().toString().trim();

        if (title.isEmpty()) {
            showToastMessage("제목을 입력해 주세요.");
            return;
        }

        if (restaurant.isEmpty()) {
            showToastMessage("식당을 입력해 주세요.");
            return;
        }

        if (category.isEmpty()) {
            showToastMessage("카테고리를 선택해 주세요.");
            return;
        }

        if (maxPrice.isEmpty()) {
            showToastMessage("최대 금액을 입력해 주세요.");
            return;
        }

        if (maxPerson.isEmpty()) {
            showToastMessage("최대 인원수를 입력해 주세요.");
            return;
        }

        if (destination.isEmpty()) {
            showToastMessage("배달 목적지를 입력해 주세요.");
            return;
        }

        showProgressDialog("등록중 ... 잠시만 기다려 주세요.");


        db.child("GroupCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.e("firebase", "in", task.getException());
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    g_count = task.getResult().getValue(Integer.class);
                    if(g_count == null){
                        g_count = 0;
                    }
                    String count = g_count.toString();
                    ArrayList<String> userlist = new ArrayList<>();
                    userlist.add(auth.getUid());
                    Group group = new Group(
                            auth.getUid(),
                            title,
                            restaurant,
                            "미정",
                            Integer.parseInt(maxPrice),
                            Integer.parseInt(maxPerson),
                            destination,
                            count,
                            userlist);

                    db.child("Group")
                            .child("Group_"+count)
                            .setValue(group);
                    g_count += 1;
                    db.child("GroupCount").setValue(g_count);
                    db.child("UserAccount").child(auth.getUid()).child("group").setValue("Group_"+count);
                    dismissProgressDialog();
                    showToastMessage("등록되었습니다.");
                }
            }
        });


        finish();
    }
}