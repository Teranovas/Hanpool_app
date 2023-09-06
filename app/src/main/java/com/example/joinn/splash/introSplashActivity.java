package com.example.joinn.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joinn.R;
import com.example.joinn.matchingActivity;


public class introSplashActivity extends AppCompatActivity {

    private ConstraintLayout container;

    private static final String TAG = "introSplashActivity";

    private TextView text1;
    private TextView text2;
    private TextView text3;

    private int currentTextView = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_splash);

        text1 = findViewById(R.id.Text1);
        text2 = findViewById(R.id.Text2);
        text3 = findViewById(R.id.Text3);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 보이는 텍스트뷰를 다음 텍스트뷰로 전환하면서 애니메이션 적용
                switch (currentTextView) {
                    case 1:
                        showWithAnimation(text2);
                        currentTextView = 2;
                        break;
                    case 2:
                        showWithAnimation(text3);
                        currentTextView = 3;
                        break;
                    case 3:
                        currentTextView = 1;
                        break;
                }
            }
        };

        // 아무 화면이나 클릭할 때마다 클릭 이벤트 처리
        findViewById(android.R.id.content).setOnClickListener(clickListener);
    }

    // 텍스트뷰를 페이드 인 애니메이션과 함께 나타나게 함
    private void showWithAnimation(final TextView textView) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        textView.startAnimation(alphaAnimation);
    }
}