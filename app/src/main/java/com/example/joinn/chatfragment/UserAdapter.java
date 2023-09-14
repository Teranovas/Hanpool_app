package com.example.joinn.chatfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joinn.R;

import java.util.List;


public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resource;
    private List<User> userList;

    public UserAdapter(Context context, int resource, List<User> userList) {
        super(context, resource, userList);
        this.context = context;
        this.resource = resource;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        User user = userList.get(position);

        ImageView imageView = convertView.findViewById(R.id.userImageView);
        TextView userTextView = convertView.findViewById(R.id.chatUserView);

        userTextView.setText(user.getWriter());

        Glide.with(context).load(user.getImageUrl()).into(imageView);

        return convertView;
    }
}
