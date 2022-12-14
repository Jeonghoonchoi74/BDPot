package com.example.baedalpot.ui.writepost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class WritePostActivity extends BaseActivity{
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.menuSpinner.setAdapter(adapter);

        //((AutoCompleteTextView) binding.categoryTextField.getEditText()).setAdapter(adapter);

        binding.submitButton.setOnClickListener(v -> submit());
    }

    private void submit() {
        if (isProgressDialogVisible()) return;

        String title = binding.titleTextField.getEditText().getText().toString().trim();
        String restaurant = binding.restaurantTextField.getEditText().getText().toString().trim();
        String category = binding.menuSpinner.getSelectedItem().toString();
        String maxPrice = binding.maxPriceTextField.getEditText().getText().toString().trim();
        String maxPerson = binding.maxPersonTextField.getEditText().getText().toString().trim();
        String destination = binding.destinationTextField.getEditText().getText().toString().trim();

        if (title.isEmpty()) {
            showToastMessage("????????? ????????? ?????????.");
            return;
        }

        if (restaurant.isEmpty()) {
            showToastMessage("????????? ????????? ?????????.");
            return;
        }

        if (category.isEmpty()) {
            showToastMessage("??????????????? ????????? ?????????.");
            return;
        }

        if (maxPrice.isEmpty()) {
            showToastMessage("?????? ????????? ????????? ?????????.");
            return;
        }

        if (maxPerson.isEmpty()) {
            showToastMessage("?????? ???????????? ????????? ?????????.");
            return;
        }

        if (destination.isEmpty()) {
            showToastMessage("?????? ???????????? ????????? ?????????.");
            return;
        }

        showProgressDialog("????????? ... ????????? ????????? ?????????.");


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
                            category,
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
                    showToastMessage("?????????????????????.");
                }
            }
        });


        finish();
    }

}