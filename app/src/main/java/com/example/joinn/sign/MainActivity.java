package com.example.joinn.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joinn.R;
import com.example.joinn.matchingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText emailText, passwordText;
    private Button joinBtn, loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.EmailText);
        passwordText = findViewById(R.id.PasswordText);
        joinBtn = findViewById(R.id.joinbtn);
        loginBtn = findViewById(R.id.loginbtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                // Firebase 인증을 사용하여 로그인 시도
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공 시
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, matchingActivity.class);
                                    startActivity(intent);
                                } else {
                                    // 로그인 실패 시
                                    Toast.makeText(MainActivity.this, "로그인 실패. 이메일 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity", "joinBtn 클릭됨");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}