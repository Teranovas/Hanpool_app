package com.example.joinn.homefragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joinn.R;

public class HomeFragment extends Fragment {

    private Button loadSearchBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadSearchBtn = view.findViewById(R.id.loadSearch_btn);
        loadSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right, // SearchFragment로 슬라이드 애니메이션 적용
                                R.anim.slide_out_left // HomeFragment에서 슬라이드 애니메이션 적용
                        )
                        .replace(R.id.fragment_container, searchFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}