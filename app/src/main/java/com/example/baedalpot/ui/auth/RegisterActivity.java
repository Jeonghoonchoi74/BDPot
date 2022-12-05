package com.example.baedalpot.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.example.baedalpot.BaseActivity;
import com.example.baedalpot.ui.home.NaviActivity;
import com.example.baedalpot.databinding.ActivityRegisterBinding;
import com.example.baedalpot.model.User;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance(); //파이어베이스 인증 처리
    private final FirebaseDatabase db = FirebaseDatabase.getInstance(); // 실시간 데이터베이스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.accNumTextField.getEditText().setTransformationMethod(null);
        binding.btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        if (isProgressDialogVisible()) return;

        String accNum = binding.accNumTextField.getEditText().getText().toString().trim();
        String id = binding.idTextField.getEditText().getText().toString().trim();
        String email = binding.emailTextField.getEditText().getText().toString().trim();
        String password = binding.passwordTextField.getEditText().getText().toString().trim();
        //String match = "@ruu.kr"; 임시 로그인 도메인 ruu.kr로 임시 이메일 생성 가능
        if (accNum.isEmpty()) {
            Toast.makeText(this, "계좌번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (id.isEmpty()) {
            Toast.makeText(this, "아아디를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "이메일 주소를 올바르게 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "비밀번호를 최소 6자리 이상으로 입력해 주세요.", Toast.LENGTH_LONG).show();
            return;
        }


       // if(email.contains(match)){
            showProgressDialog("회원가입 중 ... 잠시만 기다려 주세요.");
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if (task.getException() != null) {
                            dismissProgressDialog();

                            Exception exception = task.getException();

                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, "이미 사용중인 이메일 주소 입니다. 다른 이메일 주소를 입력해 주세요.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                            }
                            return;
                        }

                        AuthResult result = task.getResult();
                        User user = new User(id, email, password, accNum);

                        db.getReference().child("UserAccount")
                                .child(result.getUser().getUid())
                                .setValue(user);

                        dismissProgressDialog();

                        startActivity(new Intent(this, LoginActivity.class));
                        finishAffinity();
                    });
      /*  }else{
            Toast.makeText(this, "이메일을 주소를 @mju.ac.kr로 끝나야 됩니다.", Toast.LENGTH_LONG).show();
        }*/

    }
}
