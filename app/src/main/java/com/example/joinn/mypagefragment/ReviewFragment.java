package com.example.joinn.mypagefragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ReviewFragment extends Fragment {

    private ListView reviewListView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        reviewListView = view.findViewById(R.id.reviewlistView);

        loadReviews();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return view;
    }

    private void loadReviews() {
        // Firebase 데이터베이스의 참조를 가져옵니다.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carpoolReview").child(uid); // UID는 현재 로그인한 사용자의 UID입니다.
        Log.e(TAG, "나의 uid"+ uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewList.clear();

                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    reviewList.add(review);
                }

                reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
                reviewListView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "Failed to read post data.");
                // 오류 처리...
            }
        });
    }
}
