package com.example.joinn.homefragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joinn.R;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.example.joinn.mypagefragment.DriverRegistrationFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button loadSearchBtn;

    private RecyclerView routeRecyclerView;
    private RouteAdapter routeAdapter;
    private List<Route> routeList;

    DatabaseReference similarRoutesRef = FirebaseDatabase.getInstance().getReference("similarRoutes");

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadSearchBtn = view.findViewById(R.id.loadSearch_btn);

        routeRecyclerView = view.findViewById(R.id.recycler_view);
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        routeList = new ArrayList<>();
        // 예제 데이터 추가 (실제 앱에서는 데이터베이스나 API에서 데이터를 가져올 수 있습니다.)


        routeAdapter = new RouteAdapter(getActivity(), routeList);
        routeRecyclerView.setAdapter(routeAdapter);

        loadSimilarRoutes();



        loadSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new SearchFragment();

                transaction.replace(R.id.container, newFragment);

                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;
    }

    private void loadSimilarRoutes() {
        String currentUserId = FirebaseAuth.getInstance().getUid();

        if (currentUserId == null) return; // 로그인이 되어 있지 않다면 반환

        similarRoutesRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    com.example.joinn.homefragment.LatLng startLatLng = new com.example.joinn.homefragment.LatLng(
                            snapshot.child("start").child("latitude").getValue(Double.class),
                            snapshot.child("start").child("longitude").getValue(Double.class)
                    );
                    com.example.joinn.homefragment.LatLng endLatLng = new com.example.joinn.homefragment.LatLng(
                            snapshot.child("end").child("latitude").getValue(Double.class),
                            snapshot.child("end").child("longitude").getValue(Double.class)
                    );
                    Route route = new Route(startLatLng, endLatLng);
                    routeList.add(route);
                }
                routeAdapter.notifyDataSetChanged(); // 데이터 변경 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
            }
        });
    }



}