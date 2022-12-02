package com.example.baedalpot.ui.writepost;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.baedalpot.BaseActivity;
import com.example.baedalpot.R;
import com.example.baedalpot.databinding.ActivityWritePostBinding;
import com.example.baedalpot.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WritePostActivity extends BaseActivity {
    private ActivityWritePostBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

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
        ((AutoCompleteTextView) binding.categoryTextField.getEditText()).setAdapter(adapter);

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

        Group group = new Group(
                auth.getUid(),
                title,
                restaurant,
                category,
                Integer.parseInt(maxPrice),
                Integer.parseInt(maxPerson),
                destination);

        db.getReference().child("Group")
                .push()
                .setValue(group);

        dismissProgressDialog();
        showToastMessage("등록되었습니다.");
        finish();
    }
}