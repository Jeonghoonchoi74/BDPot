package com.example.baedalpot.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.example.baedalpot.BaseActivity;
import com.example.baedalpot.ui.home.NaviActivity;
import com.example.baedalpot.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/* 있으면 회원가입 하면 바로 기능 사용 가능 (1회성으로 이메일 인증없이 사용 가능 범죄 악용 우려 ↑)
        if (auth.getCurrentUser() != null) {
            startMainActivity(false);
            return;
        }
*/

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> signIn());
        binding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void signIn() {
        if (isProgressDialogVisible()) return;

        String email = binding.emailTextField.getEditText().getText().toString().trim();
        String password = binding.passwordTextField.getEditText().getText().toString().trim();

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

        showProgressDialog("로그인 중 ... 잠시만 기다려 주세요.");

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.getException() != null) {
                        dismissProgressDialog();

                        Exception exception = task.getException();

                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(this, "해당 이메일 주소로 가입된 계정이 없습니다.", Toast.LENGTH_LONG).show();
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                        }

                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        startMainActivity(true);

                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "이메일을 활성화를 시켜주십시오",
                                Toast.LENGTH_SHORT).show();

                    }
                    dismissProgressDialog();
                });
    }

    private void startMainActivity(boolean isAnimated) {
        Intent intent = new Intent(this, NaviActivity.class);
        if (!isAnimated) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        startActivity(intent);
        finish();

        if (isAnimated) {
            overridePendingTransition(0, 0);
        }
    }
}
