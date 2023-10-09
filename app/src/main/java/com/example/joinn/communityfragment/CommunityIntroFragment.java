package com.example.joinn.communityfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.joinn.R;
import com.example.joinn.mypagefragment.DateFragment;

public class CommunityIntroFragment extends Fragment {

    private ImageButton toHomeBtn, toSchoolBtn, toTaxiBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_intro, container, false);

        toHomeBtn = view.findViewById(R.id.toHomeBtn);
        toSchoolBtn = view.findViewById(R.id.toSchoolBtn);
        toTaxiBtn =  view.findViewById(R.id.taxiBtn);

        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);


        toHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHomeBtn.startAnimation(buttonClickAnimation);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Fragment newFragment = new AfterFragment();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        toSchoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSchoolBtn.startAnimation(buttonClickAnimation);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Fragment newFragment = new CommunityFragment();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        toTaxiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toTaxiBtn.startAnimation(buttonClickAnimation);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Fragment newFragment = new TaxiFragment();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;


    }
}