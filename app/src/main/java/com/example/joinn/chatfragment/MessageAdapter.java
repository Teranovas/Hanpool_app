package com.example.joinn.chatfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joinn.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private int resource;
    private List<Message> messageList;

    public MessageAdapter(Context context, int resource, List<Message> messageList) {
        super(context, resource, messageList);
        this.context = context;
        this.resource = resource;
        this.messageList = messageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        }

        Message message = messageList.get(position);
        TextView messageTextView = convertView.findViewById(R.id.messageTextViewSelf); // 본인 메시지
        LinearLayout messageLayoutSelf = convertView.findViewById(R.id.messageLayoutSelf);
        TextView messageTextViewOther = convertView.findViewById(R.id.messageTextViewOther); // 상대방 메시지
        LinearLayout messageLayoutOther = convertView.findViewById(R.id.messageLayoutOther);

        // 메시지 내용 설정
        if (message.isSelf()) {
            messageTextViewOther.setVisibility(View.GONE); // 상대방 메시지 뷰 숨기기
            messageLayoutOther.setVisibility(View.GONE); // 상대방 메시지 레이아웃 숨기기
            messageTextView.setText(message.getMessage());
            messageLayoutSelf.setVisibility(View.VISIBLE); // 본인 메시지 레이아웃 보이기
        } else {
            messageTextView.setVisibility(View.GONE); // 본인 메시지 뷰 숨기기
            messageLayoutSelf.setVisibility(View.GONE); // 본인 메시지 레이아웃 숨기기
            messageTextViewOther.setText(message.getMessage());
            messageLayoutOther.setVisibility(View.VISIBLE); // 상대방 메시지 레이아웃 보이기
        }

        return convertView;
    }
}

