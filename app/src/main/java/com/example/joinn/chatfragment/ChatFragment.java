package com.example.joinn.chatfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joinn.R;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ListView userListView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        userListView = view.findViewById(R.id.chat_listview);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(requireContext(), userList);
        userListView.setAdapter(userAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String nickname = bundle.getString("nickname");
            String profileImage= bundle.getString("profileImage");

            // 받아온 닉네임을 User 객체로 생성하여 userList에 추가합니다.
            User user = new User(nickname, null); // 프로필 사진은 일단 null로 설정합니다.
            userList.add(user);
            userAdapter.notifyDataSetChanged();
        }

        return view;
    }
}
