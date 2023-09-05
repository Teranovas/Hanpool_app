package com.example.joinn.chatfragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.joinn.R;
import com.example.joinn.chatfragment.User;
import com.example.joinn.chatfragment.UserAdapter;
import com.example.joinn.communityfragment.DetailFragment;
import com.example.joinn.communityfragment.Post;
import com.example.joinn.communityfragment.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatFragment extends Fragment {

    private List<User> userList;
    private ListView listView;
    private UserAdapter userAdapter;
    private DatabaseReference chatListRef; // 채팅방 리스트 데이터를 저장하는 레퍼런스

    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;


    private DatabaseReference usersRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = view.findViewById(R.id.chatlistView);
        userList = new ArrayList<>();

        userAdapter = new UserAdapter(getActivity(), R.layout.user_item, userList);
        listView.setAdapter(userAdapter);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserName = currentUser.getUid();

            usersRef.child(currentUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userNickname = dataSnapshot.child("닉네임").getValue(String.class);

                        DatabaseReference chatListRef = FirebaseDatabase.getInstance().getReference().child("chatList").child(userNickname);
                        chatListRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userList.clear();
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);
                                    userList.add(user);
                                }
                                // 시간순으로 정렬
                                Collections.sort(userList, new Comparator<User>() {
                                    @Override
                                    public int compare(User user1, User user2) {
                                        return Long.compare(user1.getTimestamp(), user2.getTimestamp());
                                    }
                                });
                                userAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Failed to read post data.", error.toException());
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 처리 중 오류 발생 시
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = userList.get(position);

                String opponentNickname = selectedUser.getWriter();

                String opponentImageUrl = selectedUser.getImageUrl();
                // Firebase Realtime Database에서 해당 닉네임을 가진 사용자를 찾습니다.
                usersRef.orderByChild("닉네임").equalTo(opponentNickname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String opponentUID = userSnapshot.getKey(); // 해당 사용자의 UID를 가져옵니다.

                            // 이제 opponentUID를 사용하여 원하는 작업을 수행할 수 있습니다.
                            Log.d(TAG, "상대방의 UID: " + opponentUID);

                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("opponentNickname", opponentNickname);
                            bundle.putString("opponentImageUrl", opponentImageUrl);
                            bundle.putString("opponentUID", opponentUID);

                            chatRoomFragment.setArguments(bundle);
                            transaction.replace(R.id.container, chatRoomFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "데이터베이스에서 사용자를 찾는 중 오류 발생", databaseError.toException());
                    }
                });
            }
        });


        return view;
    }
}



