package com.example.joinn.mypagefragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context context;
    private List<Review> reviews;

    DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference().child("ratings");
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

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

        // 날짜 형식을 지정합니다. 예를 들어 "2023년 1월 2일"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault());

        // Review 객체로부터 날짜 데이터를 가져와서 원하는 형식의 문자열로 변환합니다.
        String formattedDate = dateFormat.format(review.getDate());

        nicknameTextView.setText(review.getNickname());  // getter 메서드를 사용하여 닉네임을 가져옵니다.
        String opponent = review.getNickname();
        Glide.with(context).load(review.getPhotoUrl()).into(profileImageView);  // getter 메서드를 사용하여 이미지 URL을 가져옵니다.
        dateTextView.setText(formattedDate);  // getter 메서드를 사용하여 시간 정보를 가져와서 문자열로 변환 후 설정합니다.

        Button reviewButton = convertView.findViewById(R.id.reviewButton); // 버튼의 id를 확인하세요
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 대화상자를 표시하는 메소드 호출
                showRatingDialog(position, opponent);
            }
        });

        return convertView;
    }

    private void showRatingDialog(int position, String opponent) {
        // 대화상자 레이아웃을 인플레이트합니다.
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);
        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        // 대화상자를 생성합니다.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setTitle("별점 매기기")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 여기에서 별점을 처리합니다.
                        float rating = ratingBar.getRating();
                        usersRef.orderByChild("닉네임").equalTo(opponent).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    String uid = userSnapshot.getKey();
                                    ratingsRef.child(opponent).push().setValue(rating); // 별점을 ratings 경로에 추가
                                }
                                Map<String, Float> userRatingsMap = new HashMap<>();
                                ratingsRef.child(opponent).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        float totalRating = 0;
                                        int ratingCount = 0;
                                        for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                                            float rating = ratingSnapshot.getValue(Float.class);
                                            totalRating += rating;
                                            ratingCount++;
                                        }

                                        float averageRating = totalRating / ratingCount;
                                        DecimalFormat df = new DecimalFormat("#.#");
                                        String formattedRating = df.format(averageRating);
                                        usersRef.orderByChild("닉네임").equalTo(opponent).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                    String uid = userSnapshot.getKey();
                                                    usersRef.child(uid).child("평점").setValue(formattedRating);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Review review = reviews.get(position);
                        // 별점을 서버에 보내거나 로컬 데이터베이스에 저장하는 로직을 구현합니다.
                    }
                })
                .setNegativeButton("취소", null);
        builder.create().show();
    }
}
