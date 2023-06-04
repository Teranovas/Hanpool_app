package com.example.joinn.homefragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joinn.R;
import com.example.joinn.mypagefragment.DriverRegistrationFragment;

public class HomeFragment extends Fragment {

    private Button loadSearchBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadSearchBtn = view.findViewById(R.id.loadSearch_btn);
        loadSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HomeFragment와 SearchFragment가 겹쳐서 보이게 하는 코드
//                SearchFragment searchFragment = new SearchFragment();
//                getParentFragmentManager().beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.slide_in_right, // SearchFragment로 슬라이드 애니메이션 적용
//                                R.anim.slide_out_left // HomeFragment에서 슬라이드 애니메이션 적용
//                        )
//                        .replace(R.id.fragment_container, searchFragment)
//                        .addToBackStack(null)
//                        .commit();
                //겹쳐지지 않고 새로운 화면으로 전환되는 코드
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new SearchFragment();

                transaction.replace(R.id.container, newFragment);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}