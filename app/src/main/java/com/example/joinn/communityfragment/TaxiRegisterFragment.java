package com.example.joinn.communityfragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class TaxiRegisterFragment extends Fragment {

    private TextView startTextView;

    private TextView arriveTextView;

    private Button savebtn;

    String startData;
    String arriveData;

    private DatabaseReference postsRef;

    private StorageReference storageRef;

    private DatabaseReference usersRef;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taxi_register, container, false);
        startTextView = view.findViewById(R.id.start);
        arriveTextView = view.findViewById(R.id.arrive);
        savebtn = view.findViewById(R.id.SaveBtn);

        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                Fragment newFragment = new TaxistartFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        if(getArguments() != null){
            startData = getArguments().getString("startdata");
            startTextView.setText(startData);


        }

        arriveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();

                Fragment newFragment = new TaxiArriveFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        if(getArguments() != null){
            arriveData = getArguments().getString("arrivedata");
            arriveTextView.setText(arriveData);


        }
        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Bundle bundle = getArguments();


                String postId = bundle.getString("postId");


                String userId = bundle.getString("userId");
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                String title = bundle.getString("title");


                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String writer = snapshot.child("닉네임").getValue(String.class);
                            String imageUrl = snapshot.child("photoUrl").getValue(String.class);
                            StorageReference imageRef = storageRef.child("images/" + postId + ".jpg");

                            long timestamp = System.currentTimeMillis();
                            Log.d(TAG,"시간" +  timestamp);
                            Post post = new Post(postId, title, startData, arriveData, writer, imageUrl, timestamp, true);

                            postsRef.child(postId).setValue(post);
                            Toast.makeText(getContext(), "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                            // TaxiFragment로 이동
                            TaxiFragment taxiFragment = new TaxiFragment();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, taxiFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return view;
    }
}
