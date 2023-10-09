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

import java.util.Random;

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

    private void signUp(final String email, String password) {

        if (!isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원 가입 성공
                            sendVerificationEmail(email); // 인증 이메일 보내기
                        } else {
                            // 회원 가입 실패
                            Toast.makeText(LoginActivity.this, "회원 가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        // 이메일을 검증하기 위한 정규 표현식
        String emailPattern = "[a-zA-Z0-9._%+-]+@hs\\.ac\\.kr";

        // 이메일이 정규 표현식과 일치하는지 확인
        return email.matches(emailPattern);
    }

    private void sendVerificationEmail(final String email) {
        // 6자리 랜덤 인증 번호 생성 (이 부분을 적절히 수정하여 인증 번호 생성)
        final String verificationCode = generateRandomCode();

        // Firebase에 이메일 인증 요청 보내기
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 이메일 전송 성공
                            // 인증 번호를 verifyEdit EditText에 설정하고 verifyEdit 및 verifyBtn을 표시
                            EditText verifyEdit = findViewById(R.id.verifyEdit);
                            verifyEdit.setVisibility(View.VISIBLE);

                            Button verifyBtn = findViewById(R.id.verifyBtn);
                            verifyBtn.setVisibility(View.VISIBLE);
                            verifyEdit.setText(verificationCode);

                            // verifyBtn 클릭 리스너 추가
                            verifyBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 사용자가 입력한 인증 번호
                                    String userEnteredCode = verifyEdit.getText().toString().trim();
                                    if (userEnteredCode.equals(verificationCode)) {
                                        // 인증 번호 일치, 다음 화면으로 이동
                                        startActivity(new Intent(LoginActivity.this, NicknameActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "인증 번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } else {
                            // 이메일 전송 실패
                            Toast.makeText(LoginActivity.this, "이메일을 보내는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private String generateRandomCode() {
        // 6자리 랜덤 숫자 생성
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000에서 999999 사이의 숫자 생성
        return String.valueOf(code);
    }
//checking

}