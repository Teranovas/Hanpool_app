package com.example.joinn.chatfragment;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Handler;
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
import com.example.joinn.communityfragment.RegisterFragment;
import com.example.joinn.databinding.FragmentChatRoomBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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

    private FirebaseAuth Auth;

    private FirebaseUser currentUser;

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
        messageAdapter = new MessageAdapter(requireContext(), messageList, onCarpoolDateAcceptedListener);


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
                                                binding.chatRecyclerView.scrollToPosition(messageList.size() - 1);
                                            });
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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

                        binding.chatRecyclerView.scrollToPosition(messageList.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Error: " + error.getMessage());
                    }
                });

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.child(senderUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String position = snapshot.child("직위").getValue(String.class);

                        if ("드라이버".equals(position)) { // 직위 "드라이버"인 경우만 추가

                            showDateSelectionDialog();

                        }
                        else{
                            Toast.makeText(getContext(), "드라이버 등록을 해주세요!", Toast.LENGTH_SHORT).show();
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

    private void showDateSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CalendarView calendarView = new CalendarView(getContext());

        // 1. 선택된 날짜를 저장하기 위한 변수를 추가
        final Date[] selectedDateHolder = new Date[1];

        // 2. 날짜 변경 리스너 설정
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateHolder[0] = new GregorianCalendar(year, month, dayOfMonth).getTime();
        });


        builder.setView(calendarView);
        builder.setTitle("날짜 선택하기");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selectedDateHolder[0] != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
                    String formattedDate = dateFormat.format(selectedDateHolder[0]);

                    String invitationMessage = formattedDate + " 카풀에 초대합니다.";
                    sendInvitationMessageToReceiver(invitationMessage);
                }
            }
        });

        builder.create().show();
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
                                        binding.chatRecyclerView.scrollToPosition(messageList.size() - 1);
                                    });
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }
        });
    }

    private void saveCarpoolPlan(String date) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carpoolRef = FirebaseDatabase.getInstance().getReference("carpoolPlans").child(uid);
        carpoolRef.push().setValue(date);
        DatabaseReference receiverCarpoolRef = FirebaseDatabase.getInstance().getReference("carpoolPlans").child(receiverUid);
        receiverCarpoolRef.push().setValue(date);
        Log.d(TAG, "Receiver UID: " + receiverUid);
        carpoolRef.push().setValue(date);


        DatabaseReference carpoolReview = FirebaseDatabase.getInstance().getReference("carpoolReview").child(uid);

        long now = System.currentTimeMillis();

        usersRef.child(receiverUid).addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Nickname = snapshot.child("닉네임").getValue(String.class);
                String imageUrl = snapshot.child("photoUrl").getValue(String.class);

                // 24시간 후에 실행될 코드를 정의합니다.
                Runnable saveReviewRunnable = new Runnable() {
                    @Override
                    public void run() {
                        carpoolReview.child(receiverUid).child("nickname").setValue(Nickname);
                        carpoolReview.child(receiverUid).child("date").setValue(now);
                        carpoolReview.child(receiverUid).child("photoUrl").setValue(imageUrl);
                    }
                };

                // 24시간 후에 saveReviewRunnable을 실행합니다.
                new Handler().postDelayed(saveReviewRunnable, 86400000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface OnCarpoolDateAcceptedListener {
        void onDateAccepted(String date);
    }

    private OnCarpoolDateAcceptedListener onCarpoolDateAcceptedListener = new OnCarpoolDateAcceptedListener() {
        @Override
        public void onDateAccepted(String date) {
            saveCarpoolPlan(date);
        }
    };

}
