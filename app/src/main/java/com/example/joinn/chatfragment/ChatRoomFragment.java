package com.example.joinn.chatfragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.chatfragment.Message;
import com.example.joinn.chatfragment.MessageAdapter;
import com.example.joinn.databinding.FragmentChatRoomBinding;
import com.example.joinn.mypagefragment.DateFragment;
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

public class ChatRoomFragment extends Fragment implements MessageAdapter.OnAcceptButtonClickListener, DateFragment.DateSelectionListener {

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
    private DateFragment dateFragment;

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
        senderUid = mAuth.getCurrentUser().getUid();
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
                String message = binding.messageEdit.getText().toString();
                usersRef.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Nickname = snapshot.child("닉네임").getValue(String.class);
                        Message messageObject = new Message(message, senderUid, getCurrentTime(), Nickname);
                        messageObject.setAcceptButtonVisible(false);
                        messageObject.setRejectButtonVisible(false);
                        messageList.add(messageObject);
                        messageAdapter.notifyDataSetChanged();

                        mDbRef.child("chats").child(senderRoom).child("messages").push()
                                .setValue(messageObject)
                                .addOnSuccessListener(aVoid -> {
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject)
                                            .addOnSuccessListener(aVoid2 -> {
                                                messageAdapter.notifyDataSetChanged();
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
                // Show DateFragment when the invite button is clicked
                if (dateFragment == null) {
                    dateFragment = new DateFragment();
                }

                dateFragment.setDateSelectionListener(ChatRoomFragment.this);

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.dateFragmentContainer, dateFragment)
                        .commit();
            }
        });

        return view;
    }

   public void onAcceptButtonClick() {
       if (selectedDate != null) {
           // 선택된 날짜를 문자열로 변환
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           String selectedDateString = sdf.format(selectedDate);

           // DateFragment로 선택된 날짜를 전달하기 위한 Bundle을 생성합니다.
           Bundle bundle = new Bundle();
           bundle.putString("selectedDate", selectedDateString);

           Toast.makeText(getActivity(), "카풀 일정이 등록되었습니다.", Toast.LENGTH_SHORT).show();

           // DateFragment를 생성하고 선택된 날짜를 전달합니다.
           DateFragment dateFragment = new DateFragment();
           dateFragment.setArguments(bundle);
       } else {
           Toast.makeText(getActivity(), "날짜를 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
       }
   }

    @Override
    public void onDateSelected(String selectedDate) {
        // Handle the selected date from DateFragment
        // This method is called when a date is selected in DateFragment
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
}
