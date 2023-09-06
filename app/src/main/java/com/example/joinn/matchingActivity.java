package com.example.joinn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.joinn.communityfragment.CommunityFragment;
import com.example.joinn.communityfragment.CommunityIntroFragment;
import com.example.joinn.mapfragment.MapFragment;
import com.example.joinn.homefragment.HomeFragment;
import com.example.joinn.chatfragment.ChatFragment;
import com.example.joinn.mypagefragment.MyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class matchingActivity extends AppCompatActivity {
    public CommunityIntroFragment getCommunityFragment() {
        return communityintroFragment;
    }
    CommunityIntroFragment communityintroFragment;
    MapFragment mapFragment;
    HomeFragment homeFragment;
    ChatFragment chatFragment;
    MyPageFragment myPageFragment;

    BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        communityintroFragment = new CommunityIntroFragment();
        mapFragment = new MapFragment();
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        myPageFragment = new MyPageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, communityintroFragment).commit();
                        return true;
                    case R.id.map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
                        return true;
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
                        return true;
                    case R.id.Mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
