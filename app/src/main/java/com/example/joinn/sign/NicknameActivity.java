package com.example.joinn.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NicknameActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    EditText NicknameText;
    Button savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        firebaseAuth = FirebaseAuth.getInstance(); //이 구문은 현재 Firebase 인증 서비스의 인스턴스를 가져옴.

        savebtn =  findViewById(R.id.SaveBtn);
        NicknameText = findViewById(R.id.NickNameText);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = NicknameText.getText().toString().trim();
                FirebaseUser user = firebaseAuth.getCurrentUser();  // 현재 로그인한 사용자 정보 firebaseAuth이용하여 가져옴.
                String uid = user.getUid();
                String level = "1";

                HashMap<Object, String> hashMap = new HashMap<>();
                // 이전 Activity에서 전달된 데이터를 HashMap 형태로 가져옴

                hashMap.put("uid", uid);
                hashMap.put("닉네임",nickname);
                hashMap.put("드라이버 레벨", level);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Firebase 데이터베이스 인스턴스를 가져옴.
                DatabaseReference reference = database.getReference("users");
                // DatabaseReference 객체를 가져와 "users"라는 레퍼런스를 가진 노드를 참조
                reference.child(uid).setValue(hashMap);
                // Firebase 데이터베이스에 현재 사용자의 uid에 hashMap을 저장

                Toast.makeText(NicknameActivity.this, "다음", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NicknameActivity.this, ImageActivity.class);
                intent.putExtra("userData", hashMap);
                startActivity(intent);
                finish();
            }
        });
    }
}