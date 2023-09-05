package com.example.joinn.sign;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.joinn.R;
import com.example.joinn.matchingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private TextView circleTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleTextview = findViewById(R.id.Appname);

////        GradientDrawable drawable = new GradientDrawable();
//////        drawable.setShape(GradientDrawable.OVAL); // 원 모양 설정
////////        drawable.setColor(Color.parseColor("#FFFFFF"));
////        circleTextview.setBackground(drawable);
        // 구글 로그인 클라이언트 초기화
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();

        // 구글 로그인 버튼 클릭 이벤트 처리
        SignInButton signInButton = findViewById(R.id.loginbtn);
        signInButton.setOnClickListener(view -> {
            // Firebase에 로그인한 사용자가 있는지 확인
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                //이미 로그인한 기록이 있다면 바로 메인화면으로
                Intent intent = new Intent(MainActivity.this, matchingActivity.class);
                startActivity(intent);
            }else{
                //회원가입 진행
                Intent intent = new Intent(MainActivity.this, NicknameActivity.class);
                startActivity(intent);
            }
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인 인텐트 결과 처리
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인이 성공하면 Firebase에 인증 정보를 등록
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // 구글 로그인 실패
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    // Firebase에 구글 로그인 정보를 등록
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase 인증 성공
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Intent intent = new Intent(MainActivity.this, NicknameActivity.class);
                        startActivity(intent);
                    } else {
                        // Firebase 인증 실패
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    // 로그인 성공 시 화면 업데이트 처리
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Firebase 인증 정보가 있을 때 처리할 로직 작성
            // 예: 화면 이동, 데이터 조회 등
            Toast.makeText(MainActivity.this, "Google 로그인 성공", Toast.LENGTH_SHORT).show();

        } else {
            // Firebase 인증 정보가 없을 때 처리할 로직 작성
            Toast.makeText(MainActivity.this, "Google 로그인 실패", Toast.LENGTH_SHORT).show();
        }
    }


}