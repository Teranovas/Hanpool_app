package com.example.joinn.communityfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.chatfragment.ChatFragment;
import com.example.joinn.chatfragment.User;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView  startTextView;
    private TextView arriveTextView;

    private TextView timestampTextView;

    private TextView writerTextView;


    private ImageView detailProfileImageView;


    private Button submmitBtn;

    private DatabaseReference usersRef;

    private DatabaseReference chatsRef;

    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserId;
    private String currentUserName;

    String imageURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatsRef = FirebaseDatabase.getInstance().getReference().child("chats");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);


        detailProfileImageView = view.findViewById(R.id.detailProfileImageView);

        titleTextView = view.findViewById(R.id.title_tv);
        startTextView = view.findViewById(R.id.start);
        arriveTextView = view.findViewById(R.id.arrive);
        timestampTextView = view.findViewById(R.id.time);
        writerTextView = view.findViewById(R.id.nickname);
        submmitBtn = view.findViewById(R.id.SummitBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle arguments = getArguments();
        if(arguments != null) {
            String title = arguments.getString("title");
            titleTextView.setText(title);

            String start = arguments.getString("start");
            startTextView.setText(start);

            String arrive = arguments.getString("arrive");
            arriveTextView.setText(arrive);

            String timestamp = arguments.getString("timestamp");
            timestampTextView.setText(timestamp);

            String writer =  arguments.getString("writer");
            writerTextView.setText(writer);

            imageURL = arguments.getString("image");

            Glide.with(this)
                    .load(imageURL)
                    .into(detailProfileImageView);



        }


        submmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    String currentUserId = currentUser.getUid();

                    usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String currentUserName = snapshot.child("닉네임").getValue(String.class);
                                String writer = writerTextView.getText().toString();

                                if (!currentUserName.equals(writer)) {
                                    // 현재 로그인한 사용자와 게시물 작성자가 다른 경우에만 채팅 화면으로 이동
                                    Toast.makeText(getContext(), "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    ChatFragment chatFragment = new ChatFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("chatOpponent", writer);
                                    bundle.putString("image", imageURL);
                                    bundle.putString("my",currentUserName);
                                    chatFragment.setArguments(bundle);
                                    transaction.replace(R.id.container, chatFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();


                                } else {
                                    // 현재 로그인한 사용자가 게시물 작성자인 경우 처리할 내용 추가
                                    Toast.makeText(getContext(), "자기 자신한테는 신청할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 사용자 데이터가 없는 경우 처리
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 에러 처리
                        }
                    });
                }
            }
        });





        return view;
    }




    
    
}
