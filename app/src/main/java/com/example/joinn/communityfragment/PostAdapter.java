package com.example.joinn.communityfragment;

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

public class PostAdapter extends ArrayAdapter<Post> {

    private Context context;
    private int resource;
    private List<Post> postList;

    public PostAdapter(Context context, int resource, List<Post> postList) {
        super(context, resource, postList);
        this.context = context;
        this.resource = resource;
        this.postList = postList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Post post = postList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView startTextView = convertView.findViewById(R.id.startTextView);
        TextView arriveTextView = convertView.findViewById(R.id.arriveTextView);
        TextView writerTextView = convertView.findViewById(R.id.writerTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView timeView = convertView.findViewById(R.id.timeView);

        titleTextView.setText(post.getTitle());
        startTextView.setText(post.getStartpoint()); // 출발지 정보를 추가하여 표시
        arriveTextView.setText(post.getArrivepoint());
        writerTextView.setText(post.getWriter());

        // 게시물 작성 시간 계산 및 표시
        long currentTimeMillis = System.currentTimeMillis();
        long postTimeMillis = post.getTimestamp();
        long elapsedTimeMillis = currentTimeMillis - postTimeMillis;
        long elapsedMinutes = elapsedTimeMillis / (60 * 1000);


        String timeAgo;
        if (elapsedMinutes < 1) {
            timeAgo = "방금 전";
        } else if (elapsedMinutes < 60) {
            timeAgo = elapsedMinutes + "분 전";
        } else {
            long elapsedHours = elapsedMinutes / 60;
            if (elapsedHours < 24) {
                timeAgo = elapsedHours + "시간 전";
            } else {
                long elapsedDays = elapsedHours / 24;
                timeAgo = elapsedDays + "일 전";
            }
        }
        timeView.setText(timeAgo);

        // 이미지를 Glide를 사용하여 로드하고 ImageView에 표시
        Glide.with(context).load(post.getImageUrl()).into(imageView);

        return convertView;
    }
}
