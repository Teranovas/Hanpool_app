package com.example.joinn.communityfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinn.R;
import com.example.joinn.chatfragment.ChatFragment;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView  startTextView;
    private TextView arriveTextView;

    private String data1;

    private String data2;



    private Button saveBtn;

    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserId;
    private String currentUserName;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        titleTextView = view.findViewById(R.id.title_tv);
        startTextView = view.findViewById(R.id.start);
        arriveTextView = view.findViewById(R.id.arrive);
        saveBtn = view.findViewById(R.id.SaveBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddSearchFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if(getArguments() != null){
            data1 = getArguments().getString("data");
            startTextView.setText(data1);
        }

        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.child(currentUserId).child("닉네임").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        currentUserName = dataSnapshot.getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
        

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }

            private void saveButtonClicked() {

                // ChatFragment로 이동
                ChatFragment chatFragment = new ChatFragment();

                // 사용자의 닉네임과 프로필 사진을 ChatFragment에 전달하기 위해 Bundle을 사용합니다.
                Bundle bundle = new Bundle();
                bundle.putString("nickname", currentUserName);
                // 프로필 사진을 전달하는 코드를 추가해주세요. 프로필 사진을 어떤 방식으로 저장하고 가져올지에 따라 코드가 달라질 수 있습니다.

                chatFragment.setArguments(bundle);

                // Fragment 전환을 위해 FragmentManager를 사용합니다.
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, chatFragment) // R.id.fragment_container는 ChatFragment를 표시할 레이아웃의 ID입니다.
                        .addToBackStack(null)
                        .commit();
            }
        });

        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String title = bundle.getString("title");
//            String content = bundle.getString("content");
//            long timestamp = bundle.getLong("timestamp");
//
//            titleTextView.setText(title);
//            contentTextView.setText(content);
//            dateTextView.setText(getFormattedDate(timestamp));
//        }
//
//        // RecyclerView 설정
//        commentList = new ArrayList<>();
//        commentAdapter = new CommentAdapter(commentList);
//        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        commentsRecyclerView.setAdapter(commentAdapter);
//
//        addCommentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addCommentButtonClicked();
//            }
//        });

        return view;
    }




    
    
}
