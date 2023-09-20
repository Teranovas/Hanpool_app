package com.example.joinn.chatfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.databinding.FragmentChatRoomBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatRoomFragment extends Fragment implements MessageAdapter.OnAcceptButtonClickListener {

    private FragmentChatRoomBinding binding;
    private String me = "나";
    private String receiverName;
    private String receiverUid;
    private String senderUid;
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
    private Calendar calendar = Calendar.getInstance();
    private long selectedDateMillis = calendar.getTimeInMillis();
    private Date selectedDate = new Date(selectedDateMillis);

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
        messageAdapter = new MessageAdapter(requireContext(), messageList, this);

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatRecyclerView.setAdapter(messageAdapter);

        ProfileImageView = view.findViewById(R.id.profileImageView);
        NicknameTextView = view.findViewById(R.id.nicknameTextView);
        inviteBtn = view.findViewById(R.id.inviteBtn);

        mAuth = FirebaseAuth.getInstance();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        senderUid = mAuth.getCurrentUser().getUid(); // senderUid 변수에 현재 사용자의 UID 할당
        senderRoom = receiverUid + senderUid;
        receiverRoom = senderUid + receiverUid;

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

        mAuth = FirebaseAuth.getInstance();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        String senderUid = mAuth.getCurrentUser().getUid();
        senderRoom = receiverUid + senderUid;
        receiverRoom = senderUid + receiverUid;

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 채팅 메시지를 전송하는 코드 추가
                String message = binding.messageEdit.getText().toString();
                usersRef.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Nickname = snapshot.child("닉네임").getValue(String.class);
                        Message messageObject = new Message(message, senderUid, getCurrentTime(), Nickname);
                        messageObject.setAcceptButtonVisible(false); // 수락 버튼 숨김
                        messageObject.setRejectButtonVisible(false); // 거절 버튼 숨김
                        messageList.add(messageObject);
                        messageAdapter.notifyDataSetChanged();

                        // 데이터 저장 (Firebase Realtime Database에 채팅 메시지 저장)
                        mDbRef.child("chats").child(senderRoom).child("messages").push()
                                .setValue(messageObject)
                                .addOnSuccessListener(aVoid -> {
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject)
                                            .addOnSuccessListener(aVoid2 -> {
                                                // 받는 쪽에도 저장 성공하면 어댑터 갱신
                                                messageAdapter.notifyDataSetChanged();
                                            });
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 오류 처리
                    }
                });

                // 입력값 초기화
                binding.messageEdit.setText("");
            }
        });

        mDbRef.child("chats").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Message message = postSnapshot.getValue(Message.class);
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
                showDatePickerDialog();
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("날짜 선택")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection));
                String invitationMessage = sendInvitationMessage(formattedDate);

                // 채팅 메시지를 전송하는 코드 추가
                String message = binding.messageEdit.getText().toString();
                usersRef.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Nickname = snapshot.child("닉네임").getValue(String.class);
                        Message messageObject = new Message(message, senderUid, getCurrentTime(), Nickname);
                        messageObject.setAcceptButtonVisible(false); // 수락 버튼 숨김
                        messageObject.setRejectButtonVisible(false); // 거절 버튼 숨김
                        messageList.add(messageObject);
                        messageAdapter.notifyDataSetChanged();

                        // 데이터 저장 (Firebase Realtime Database에 채팅 메시지 저장)
                        mDbRef.child("chats").child(senderRoom).child("messages").push()
                                .setValue(messageObject)
                                .addOnSuccessListener(aVoid -> {
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject)
                                            .addOnSuccessListener(aVoid2 -> {
                                                // 받는 쪽에도 저장 성공하면 어댑터 갱신
                                                messageAdapter.notifyDataSetChanged();
                                            });
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 오류 처리
                    }
                });

                // 입력값 초기화
                binding.messageEdit.setText("");
            }
        });

        datePicker.show(getChildFragmentManager(), "DATE_PICKER_TAG");
    }

    private String sendInvitationMessage(String selectedDate) {
        String invitationMessage = "카풀에 초대합니다! 날짜: " + selectedDate;

        Message messageObject = new Message(invitationMessage, mAuth.getCurrentUser().getUid(), getCurrentTime(), me);
        messageObject.setAcceptButtonVisible(true);
        messageObject.setRejectButtonVisible(true); // 거절 버튼 표시
        messageList.add(messageObject);
        messageAdapter.notifyDataSetChanged();

        mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener(aVoid -> {
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                            .setValue(messageObject)
                            .addOnSuccessListener(aVoid2 -> {
                                // 받는 쪽에도 저장 성공하면 어댑터 갱신
                                messageAdapter.notifyDataSetChanged();
                            });
                });

        return invitationMessage;
    }

    @Override
    public void onAcceptButtonClick() {
        // 수락 버튼 클릭 시 수행할 동작을 여기에 추가
        // 예를 들어, 다이얼로그를 표시하거나 다른 작업을 수행할 수 있습니다.
    }

    // 현재 시간을 문자열로 반환하는 메서드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
}
