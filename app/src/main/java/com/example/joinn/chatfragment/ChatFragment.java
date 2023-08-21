package com.example.joinn.chatfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<User> userList;
    private ListView listView;
    private UserAdapter userAdapter;
    private DatabaseReference chatListRef; // 채팅방 리스트 데이터를 저장하는 레퍼런스

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        chatListRef = FirebaseDatabase.getInstance().getReference().child("chatList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        listView = view.findViewById(R.id.chatlistView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String chatOpponent = arguments.getString("chatOpponent");
            String imageUrl = arguments.getString("image");

            User user = new User();
            user.setWriter(chatOpponent);
            user.setImageUrl(imageUrl);
            userList.add(user);


            userAdapter = new UserAdapter(getActivity(), R.layout.user_item, userList);


            listView.setAdapter(userAdapter);


        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = userList.get(position);

                String opponentNickname = selectedUser.getWriter();
                String opponentImageUrl = selectedUser.getImageUrl();
                String opponentUID = selectedUser.getUid();

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
        });

        return view;
    }
}
