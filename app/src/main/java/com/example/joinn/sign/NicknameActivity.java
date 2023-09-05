package com.example.joinn.sign;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                final String nickname = NicknameText.getText().toString().trim();


                Log.d(TAG,nickname);
                // 닉네임 중복 검사를 위한 레퍼런스 설정
                DatabaseReference nicknameReference = FirebaseDatabase.getInstance().getReference("nicknames");

                // 입력한 닉네임으로 쿼리하여 해당 닉네임이 이미 존재하는지 확인
                nicknameReference.orderByValue().equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(nickname)) {
                            // 중복된 닉네임이 이미 존재하는 경우
                            Toast.makeText(NicknameActivity.this, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 중복되지 않은 닉네임인 경우 Firebase에 저장
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            String level = "1";
                            String as = "라이더";

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("닉네임", nickname);
                            hashMap.put("드라이버 레벨", level);
                            hashMap.put("직위",as);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.child(uid).setValue(hashMap);

                            // 중복 닉네임 저장 방지를 위해 사용한 닉네임을 레퍼런스에 저장
                            DatabaseReference nicknameReference = FirebaseDatabase.getInstance().getReference("nicknames");
                            nicknameReference.child(nickname).setValue(true);

                            Toast.makeText(NicknameActivity.this, "다음", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NicknameActivity.this, ImageActivity.class);
                            intent.putExtra("userData", hashMap);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리
                    }
                });
            }
        });


    }
}