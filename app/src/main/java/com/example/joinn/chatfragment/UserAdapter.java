package com.example.joinn.chatfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.joinn.R;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> userList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nicknameTextView = convertView.findViewById(R.id.nickname_tv);
            viewHolder.profileImageView = convertView.findViewById(R.id.profile_image_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);

        viewHolder.nicknameTextView.setText(user.getNickname());

        String imageUrl = user.getProfileImage();
        if (imageUrl != null) {
            Glide.with(context).load(imageUrl).into(viewHolder.profileImageView);
        } else {
            // 프로필 이미지가 없을 경우 기본 이미지를 표시합니다.
            viewHolder.profileImageView.setImageResource(R.drawable.user);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView nicknameTextView;
        ImageView profileImageView;
    }
}
