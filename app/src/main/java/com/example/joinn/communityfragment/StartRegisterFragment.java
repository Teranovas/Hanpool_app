package com.example.joinn.communityfragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
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
import com.example.joinn.mapfragment.AddSearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StartRegisterFragment extends Fragment {


    private TextView startTextView;

    private Button savebtn;
    String data;

    private String after = "한신대학교";
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    private DatabaseReference postsRef;
    private StorageReference storageRef;
    private DatabaseReference usersRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_register, container, false);
        startTextView = view.findViewById(R.id.start);
        savebtn = view.findViewById(R.id.SaveBtn);

        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                Fragment newFragment = new StartFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        if(getArguments() != null){
            data = getArguments().getString("startdata");
            startTextView.setText(data);


        }

        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Bundle bundle = getArguments();
//                bundle.putString("startdata", data);


                String postId = bundle.getString("postId");



                String userId = bundle.getString("userId");
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                String title = bundle.getString("title");
                String startpoint = data;


                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String writer = snapshot.child("닉네임").getValue(String.class);
                            String imageUrl = snapshot.child("photoUrl").getValue(String.class);
                            StorageReference imageRef = storageRef.child("images/" + postId + ".jpg");

                            long timestamp = System.currentTimeMillis();
                            Post post = new Post(postId, title, startpoint, "한신대학교", writer, imageUrl, timestamp);

                            postsRef.child(postId).setValue(post);
                            Toast.makeText(getContext(), "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                            // CommunityFragment로 이동
                            CommunityFragment communityFragment = new CommunityFragment();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, communityFragment);
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