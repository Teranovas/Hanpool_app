package com.example.joinn.mypagefragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joinn.R;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, R.layout.review_item, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        }

        Review review = reviews.get(position);

        Log.d(TAG, "Nickname: " + review.getNickname() + ", PhotoURL: " + review.getPhotoUrl() + ", Date: " + review.getDate());

        TextView nicknameTextView = convertView.findViewById(R.id.reviewnickname);
        ImageView profileImageView = convertView.findViewById(R.id.profile);
        TextView dateTextView = convertView.findViewById(R.id.date); // 시간을 표시할 TextView를 가져옵니다.

        nicknameTextView.setText(review.getNickname());  // getter 메서드를 사용하여 닉네임을 가져옵니다.
        Glide.with(context).load(review.getPhotoUrl()).into(profileImageView);  // getter 메서드를 사용하여 이미지 URL을 가져옵니다.
        dateTextView.setText(String.valueOf(review.getDate()));  // getter 메서드를 사용하여 시간 정보를 가져와서 문자열로 변환 후 설정합니다.

        return convertView;
    }
}
