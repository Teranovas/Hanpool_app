package com.example.joinn.chatfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
        MessageAdapter messageAdapter = new MessageAdapter(requireContext(), messageList);

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
            public void onClick(View view) {
                // AlertDialog를 생성합니다.
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("날짜 선택");

                // CalendarView를 생성하고 대화 상자에 추가합니다.
                CalendarView calendarView = new CalendarView(getContext());
                builder.setView(calendarView);

                // OK 버튼을 추가하고 클릭 리스너를 설정합니다.
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 여기서 선택한 날짜를 처리하거나 저장할 수 있습니다.
                        long selectedDateMillis = calendarView.getDate();
                        // 선택한 날짜에 대한 작업을 수행합니다.
                    }
                });

                // Cancel 버튼을 추가하고 클릭 리스너를 설정합니다.
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소 버튼을 눌렀을 때 수행할 작업을 추가할 수 있습니다.
                        dialogInterface.dismiss();
                    }
                });

                // AlertDialog를 표시합니다.
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }
}