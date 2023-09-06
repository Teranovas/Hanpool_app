package com.example.joinn.splash;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinn.R;
import com.example.joinn.matchingActivity;
import com.example.joinn.sign.MainActivity;


public class introSplashActivity extends AppCompatActivity {

    private ConstraintLayout container;

    private static final String TAG = "introSplashActivity";

    private TextView text1;
    private TextView text2;
    private TextView text3;

    private Button startBtn;

    private int currentTextView = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_splash);

        text1 = findViewById(R.id.Text1);
        text2 = findViewById(R.id.Text2);
        text3 = findViewById(R.id.Text3);

        startBtn = findViewById(R.id.StartBtn);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 보이는 텍스트뷰를 다음 텍스트뷰로 전환하면서 애니메이션 적용
                switch (currentTextView) {
                    case 1:
                        showWithAnimation(text1);
                        currentTextView = 2;
                        break;
                    case 2:
                        showWithAnimation(text2);
                        currentTextView = 3;
                        break;
                    case 3:
                        showWithAnimation(text3);
                        currentTextView = 4;
                        break;
                    case 4:
                        showWithAnimation( startBtn );
                        break;
                }
            }
        };

        // 아무 화면이나 클릭할 때마다 클릭 이벤트 처리
        findViewById(android.R.id.content).setOnClickListener(clickListener);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(introSplashActivity.this, matchingActivity.class);
                startActivity(intent);

            }
        });
    }

    // 텍스트뷰를 페이드 인 애니메이션과 함께 나타나게 함
    private void showWithAnimation(final TextView textView) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        textView.startAnimation(alphaAnimation);
    }


}