package com.example.joinn.communityfragment;

import static android.content.ContentValues.TAG;


import com.example.joinn.mapfragment.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.joinn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommunityFragment extends Fragment {

    private List<Post> postList;
    private ListView listView;
    private PostAdapter postAdapter;
    private Button regButton;


    private Button distanceBtn;
    private Button afterBtn;
    private Button timebtn;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        listView = view.findViewById(R.id.listView);
        regButton = view.findViewById(R.id.reg_button);
        timebtn = view.findViewById(R.id.time_button);

        postList = new ArrayList<>();

        afterBtn = view.findViewById(R.id.after_button);

        distanceBtn = view.findViewById(R.id.distance_button);


        postAdapter = new PostAdapter(getActivity(), R.layout.post_item, postList);
        listView.setAdapter(postAdapter);






        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AfterFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        distanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        // onDataChange() 메서드에서 로딩 시에 시간순으로 정렬되도록 변경
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if ("한신대학교".equals(post.getArrivepoint())) { // 도착지가 "한신대학교"인 경우만 추가
                        postList.add(post);
                    }
                }
                // 시간순으로 정렬
                Collections.sort(postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2) {
                        return Long.compare(post2.getTimestamp(), post1.getTimestamp());
                    }
                });
                postAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read post data.", error.toException());
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2) {
                        return Long.compare(post2.getTimestamp(), post1.getTimestamp());
                    }
                });
                postAdapter.notifyDataSetChanged();

            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RegisterFragment로 전환
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new RegisterFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // 리스트뷰 항목 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 항목의 정보 가져오기
                Post selectedPost = postList.get(position);

                // DetailFragment로 전환하며 선택한 항목 정보 전달
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                DetailFragment detailFragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", selectedPost.getTitle());
                bundle.putString("start", selectedPost.getStartpoint());
                bundle.putString("arrive", selectedPost.getArrivepoint());
                bundle.putString("writer", selectedPost.getWriter());
                bundle.putLong("timestamp", selectedPost.getTimestamp());
                bundle.putString("image", selectedPost.getImageUrl());// 작성일 정보 전달
                detailFragment.setArguments(bundle);
                transaction.replace(R.id.container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }




}
