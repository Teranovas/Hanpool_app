package com.example.joinn.sign;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joinn.R;
import com.example.joinn.matchingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL); // 원 모양 설정
        drawable.setColor(Color.parseColor("#FFFFFF"));
        circleTextview.setBackground(drawable);
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

            signInWithGoogle();

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

    // 구글 로그인 요청
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // 구글 로그인 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    // 구글 로그인 결과를 처리하고 이메일을 확인하여 다음 단계로 이동
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String userEmail = account.getEmail();

            // Firebase Authentication을 사용하여 현재 로그인된 사용자의 이메일 주소 가져오기
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserEmail = firebaseUser != null ? firebaseUser.getEmail() : null;

            if (currentUserEmail != null && currentUserEmail.equals(userEmail)) {
                // 이미 로그인한 사용자의 이메일 주소와 일치하면 matchingActivity로 이동
                Intent intent = new Intent(this, matchingActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 이메일이 일치하지 않는 경우 다른 로그인 처리 코드 작성
                // 예: 로그인 화면으로 이동
                Toast.makeText(this, "다른 이메일로 로그인해주세요.", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w("TAG", "Google sign in failed", e);
            // 구글 로그인 실패 처리 코드 작성
        }
    }
}
