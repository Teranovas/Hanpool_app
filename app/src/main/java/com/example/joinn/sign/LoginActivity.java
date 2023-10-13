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
    private EditText emailText, passwordText,verifyEdit;
    private Button verifyBtn, joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.EmailText);
        passwordText = findViewById(R.id.PasswordText);
        verifyEdit=findViewById(R.id.verifyEdit);
        verifyBtn=findViewById(R.id.verifyBtn);
        joinBtn=findViewById(R.id.joinBtn);
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
        sendVerificationEmail(email, password); // 인증 이메일 보내기

    }

    private boolean isValidEmail(String email) {
        // 이메일을 검증하기 위한 정규 표현식
        String emailPattern = "[a-zA-Z0-9._%+-]+@hs\\.ac\\.kr";

        // 이메일이 정규 표현식과 일치하는지 확인
        return email.matches(emailPattern);
    }

    private void sendVerificationEmail(final String email, String password) {
        final String verificationCode = generateRandomCode();

        //여기서 사용자가 입력한 verifyEdit을 받아와서 해당 이메일에 verificationCode를 보내기
        new EmailSender().execute(email, verificationCode);

        verifyEdit.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.VISIBLE);
        joinBtn.setVisibility(View.INVISIBLE);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //여기서 verificationCode와 veriftEdit의 적힌 6자리 랜덤 숫자가 같다면 회원가입 진행.
                if (!verifyEdit.getText().toString().equals(verificationCode)) {
                    Toast.makeText(LoginActivity.this, "인증 코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // 회원 가입 성공
                                        Intent intent = new Intent(LoginActivity.this, NicknameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // 회원 가입 실패
                                        Toast.makeText(LoginActivity.this, "회원 가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
}