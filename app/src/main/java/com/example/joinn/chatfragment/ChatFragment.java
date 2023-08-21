package com.example.joinn.chatfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joinn.R;
import com.example.joinn.chatfragment.User;
import com.example.joinn.chatfragment.UserAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<User> userList;
    private ListView listView;
    private UserAdapter userAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        listView = view.findViewById(R.id.chatlistView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), R.layout.user_item, userList);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String chatOpponent = arguments.getString("chatOpponent");
            String imageUrl = arguments.getString("image");


            // 채팅방 리스트 데이터 설정
            List<User> userList = new ArrayList<>();
            User user = new User();
            user.setWriter(chatOpponent);
            user.setImageUrl(imageUrl);
            userList.add(user);

            userAdapter = new UserAdapter(getActivity(), R.layout.user_item, userList);
            listView.setAdapter(userAdapter);
        }



        return view;
    }



}
