package com.example.joinn.chatfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomFragment extends Fragment {

    private ListView chatListView;
    private EditText messageEditText;
    private Button sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private ImageView profileImageView;
    private TextView nicknameTextView;

    private String opponentNickname;
    private DatabaseReference chatRef;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);

        chatListView = view.findViewById(R.id.chatListView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(getActivity(), R.layout.message_item, messageList);
        chatListView.setAdapter(messageAdapter);


        profileImageView = view.findViewById(R.id.profileImageView);
        nicknameTextView = view.findViewById(R.id.nicknameTextView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            opponentNickname = arguments.getString("opponentNickname");
            String opponentImageUrl = arguments.getString("opponentImageUrl");
            String currentUserNickname = arguments.getString("currentUserNickname"); // 현재 사용자 닉네임 가져오기

            // 상대방 닉네임 설정
            nicknameTextView.setText(opponentNickname);

            // 상대방 프로필 이미지 설정 (Glide 또는 이미지 로딩 라이브러리 사용)
            Glide.with(this)
                    .load(opponentImageUrl)
                    .into(profileImageView);



        }



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
                // 메시지 전송 로직
            }
        });

        return view;
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (!messageText.isEmpty()) {
            // 메시지 객체 생성
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String senderId = mAuth.getUid(); // 실제 사용 시에는 로그인한 사용자의 ID를 사용하세요
            Message message = new Message(messageText, senderId);

            // Firebase에 메시지 저장
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(opponentNickname).child("messages").push();
            chatRef.setValue(message);

            // 메시지 리스트에 추가하고 어댑터에 변경사항 알림
            messageList.add(message);
            messageAdapter.notifyDataSetChanged();

            // 메시지 입력란 비우기
            messageEditText.setText("");

            receiveMessage(messageText, senderId);
        }
    }

    private void receiveMessage(String messageText, String senderId) {
        boolean isSelf = senderId.equals(FirebaseAuth.getInstance().getUid()); // 본인 메시지 여부 판단
        Message message = new Message(messageText, senderId);

        if (isSelf) {
            // 본인 메시지인 경우 오른쪽에 추가
            message.setSelf(true);
        } else {
            // 상대방 메시지인 경우 왼쪽에 추가
            message.setSelf(false);
        }

        messageList.add(message);
        messageAdapter.notifyDataSetChanged();
    }
}