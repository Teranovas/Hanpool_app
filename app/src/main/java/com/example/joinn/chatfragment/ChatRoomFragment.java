package com.example.joinn.chatfragment;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinn.chatfragment.ChatRoomFragment.*;



import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.databinding.FragmentChatRoomBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatRoomFragment extends Fragment {

    private FragmentChatRoomBinding binding;
    private String me = "나";

    private String receiverName;
    private String receiverUid;

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;

    private DatabaseReference usersRef;

    private String receiverRoom;
    private String senderRoom;

    private ArrayList<Message> messageList;

    private ImageView ProfileImageView;
    private TextView NicknameTextView;

    private MessageAdapter messageAdapter;

    private Button inviteBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(requireContext(), messageList);

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatRecyclerView.setAdapter(messageAdapter);

        ProfileImageView = view.findViewById(R.id.profileImageView);
        NicknameTextView = view.findViewById(R.id.nicknameTextView);

        inviteBtn = view.findViewById(R.id.inviteBtn);

        Bundle arguments = getArguments();
        if (arguments != null) {
            receiverName = arguments.getString("opponentNickname");
            receiverUid = arguments.getString("opponentUID");
            String receiverImage = arguments.getString("opponentImageUrl");

            NicknameTextView.setText(receiverName);

            Glide.with(this)
                    .load(receiverImage)
                    .into(ProfileImageView);

        }
//        receiverName = requireActivity().getIntent().getStringExtra("key1");
//        receiverUid = requireActivity().getIntent().getStringExtra("key2");

        mAuth = FirebaseAuth.getInstance();
        mDbRef = FirebaseDatabase.getInstance().getReference();

        String senderUid = mAuth.getCurrentUser().getUid();

        // 보낸이방
        senderRoom = receiverUid + senderUid;

        // 받는이방
        receiverRoom = senderUid + receiverUid;
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                String getTime = dateFormat.format(date);

                String message = binding.messageEdit.getText().toString();
                usersRef.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Nickname = snapshot.child("닉네임").getValue(String.class);
                        Message messageObject = new Message(message, senderUid, getTime, Nickname);
                        messageList.add(messageObject);
                        messageAdapter.notifyDataSetChanged();

                        // 데이터 저장
                        // 데이터 저장
                        mDbRef.child("chats").child(senderRoom).child("messages").push()
                                .setValue(messageObject)
                                .addOnSuccessListener(aVoid -> {
                                    // 저장 성공하면
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject)
                                            .addOnSuccessListener(aVoid2 -> {
                                                // 받는 쪽에도 저장 성공하면 어댑터 갱신
                                            });
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                Message messageObject = new Message(message, senderUid, getTime, me);
//                messageList.add(messageObject);
//                messageAdapter.notifyDataSetChanged();
//
//                // 데이터 저장
//                // 데이터 저장
//                mDbRef.child("chats").child(senderRoom).child("messages").push()
//                        .setValue(messageObject)
//                        .addOnSuccessListener(aVoid -> {
//                            // 저장 성공하면
//                            mDbRef.child("chats").child(receiverRoom).child("messages").push()
//                                    .setValue(messageObject)
//                                    .addOnSuccessListener(aVoid2 -> {
//                                        // 받는 쪽에도 저장 성공하면 어댑터 갱신
//                                    });
//                        });


                // 입력값 초기화
                binding.messageEdit.setText("");
            }
        });

        // 메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();

                        for (DataSnapshot postSnapshat : snapshot.getChildren()) {
                            Message message = postSnapshat.getValue(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Error: " + error.getMessage());
                    }
                });
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invitationMessage = "상대방이 당신을 카풀에 초대했습니다. 수락 하시겠습니까?";

                // 초대 메시지를 상대방에게 보내기
                sendInvitationMessageToReceiver(invitationMessage);
            }
        });

        return view;
    }

    private void sendInvitationMessageToReceiver(String invitationMessage) {
        String senderUid = mAuth.getCurrentUser().getUid();

        // 초대 메시지를 생성한 시간을 기록합니다.
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String getTime = dateFormat.format(date);

        usersRef.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Nickname = snapshot.child("닉네임").getValue(String.class);
                Message messageObject = new Message(invitationMessage, senderUid, getTime, Nickname);

                // 초대 메시지에 수락 버튼을 추가합니다.
                messageObject.setAcceptButtonVisible(true);
                // 초대 메시지에 거절 버튼을 추가합니다.
                messageObject.setRejectButtonVisible(true);

                messageList.add(messageObject);
                messageAdapter.notifyDataSetChanged();

                // 데이터 저장 (Firebase Realtime Database에 초대 메시지 저장)
                mDbRef.child("chats").child(senderRoom).child("messages").push()
                        .setValue(messageObject)
                        .addOnSuccessListener(aVoid -> {
                            // 저장 성공하면
                            mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                    .setValue(messageObject)
                                    .addOnSuccessListener(aVoid2 -> {
                                        // 받는 쪽에도 저장 성공하면 어댑터 갱신
                                    });
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }
        });
    }

    private String handleSelectedDate(long selectedDateMillis) {
        // 여기에서 선택한 날짜를 처리하거나 필요한 데이터로 변환합니다.
        Date selectedDate = new Date(selectedDateMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        String formattedDate = dateFormat.format(selectedDate);
        // formattedDate를 사용하여 선택한 날짜에 대한 작업을 수행합니다.

        String S = sendInvitationMessage(formattedDate);

        return S;
    }


    private String sendInvitationMessage(String selectedDateMillis) {
        // 여기에서 초대 메시지를 생성하고 상대방에게 보내는 작업을 수행합니다.
        // selectedDateMillis를 사용하여 초대 메시지에 선택한 날짜 정보를 포함시킵니다.

        String text = "[수락하기]";

        // 예를 들어, 초대 메시지 생성
        String invitationMessage = "카풀에 초대합니다! 날짜: " + selectedDateMillis;

        Log.d(TAG,invitationMessage);

        return invitationMessage;



        // Firebase Realtime Database에 메시지를 저장하고 상대방에게도 보냅니다.
        // 코드는 이전 답변에서 이미 제공된 내용을 활용할 수 있습니다.
    }
}