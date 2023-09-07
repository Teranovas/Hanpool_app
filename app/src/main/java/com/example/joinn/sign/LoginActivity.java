package com.example.joinn.sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joinn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.EmailText);
        passwordText = findViewById(R.id.PasswordText);

        Button joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(emailText.getText().toString(), passwordText.getText().toString());
            }
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification(user); // 이메일 인증 메일 보내기
                            Toast.makeText(LoginActivity.this, "인증메일을 확인하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "회원가입 실패.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "이메일로 인증 링크를 보냈습니다. 인증 후 로그인하세요.", Toast.LENGTH_SHORT).show();

                                // 이메일 인증이 성공한 경우 nicknameActivity로 이동
                                Intent intent = new Intent(LoginActivity.this, NicknameActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "이메일 인증 메일을 보내지 못했습니다. 나중에 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    }
