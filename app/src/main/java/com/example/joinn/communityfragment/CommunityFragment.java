package com.example.joinn.communityfragment;

import static android.content.ContentValues.TAG;
import com.example.joinn.mapfragment.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polyak.iconswitch.IconSwitch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommunityFragment extends Fragment {

    private List<Post> postList;
    private ListView listView;
    private PostAdapter postAdapter;
    private ImageButton regButton;


    private Button distanceBtn;
    private TextView afterBtn;
    private Button timebtn;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;
    private double userLatitude;  // 사용자의 현재 위도
    private double userLongitude; // 사용자의 현재 경도
    private static final int PERMISSION_REQUEST_CODE = 123; // 원하는 숫자로 대체 가능

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

        postList = new ArrayList<>();

        afterBtn = view.findViewById(R.id.after_button);

        postAdapter = new PostAdapter(getActivity(), R.layout.post_item, postList);
        listView.setAdapter(postAdapter);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // 위치 서비스 초기화
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        // 위치 정보 업데이트를 수신하기 위한 LocationListener 생성
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // 위치 업데이트를 요청
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // 위치 권한이 없는 경우 권한을 요청해야 할 수 있습니다.
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }


        IconSwitch iconSwitch = view.findViewById(R.id.icon_switch);
        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click_animation);

        iconSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                switch (current){
                    case LEFT:
                        Toast.makeText(getContext(),"거리순",Toast.LENGTH_SHORT).show();
                        // 거리순 정렬 메서드 호출
                        switchSortOrder(true);
                        break;

                    case RIGHT:
                        Toast.makeText(getContext(),"시간순",Toast.LENGTH_SHORT).show();
                        // 시간순 정렬 메서드 호출
                        switchSortOrder(false);
                        break;
                }

            }
        });

        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AfterFragment());
                transaction.addToBackStack(null);
                transaction.commit();


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
                switchSortOrder(true); // 기본적으로 거리순으로 정렬
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read post data.", error.toException());
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserName = currentUser.getUid();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                usersRef.child(currentUserName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String position = snapshot.child("직위").getValue(String.class);

                        Log.d(TAG,position);

                        if ("드라이버".equals(position)) { // 직위 "드라이버"인 경우만 추가

                            // RegisterFragment로 전환
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                            transaction.replace(R.id.container, new RegisterFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                        else{
                            Toast.makeText(getContext(), "드라이버 등록을 해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


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
    private void switchSortOrder(boolean sortByDistance) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if ("한신대학교".equals(post.getArrivepoint())) {
                        postList.add(post);
                    }
                }
                if (sortByDistance) {
                    sortByDistance();
                } else {
                    sortByTime();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read post data.", error.toException());
            }
        });
    }


    //시간순 정렬 메서드
    private void sortByTime() {
        // 시간순으로 정렬하는 코드를 여기에 추가
        // 시간순으로 postList를 정렬하고 postAdapter를 업데이트
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Long.compare(post2.getTimestamp(), post1.getTimestamp());
            }
        });

        postAdapter.notifyDataSetChanged();
    }

    // 거리순 정렬 메서드
    private void sortByDistance() {
        // 거리순으로 정렬하는 코드를 여기에 추가
        // postList를 거리순으로 정렬하고 postAdapter를 업데이트
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                // 게시물과 사용자의 현재 위치 사이의 거리를 계산
                double distanceToPost1 = calculateDistance(userLatitude, userLongitude, post1.getLatitude(), post1.getLongitude());
                double distanceToPost2 = calculateDistance(userLatitude, userLongitude, post2.getLatitude(), post2.getLongitude());
                return Double.compare(distanceToPost1, distanceToPost2);
            }
        });

        postAdapter.notifyDataSetChanged();
    }


    // 거리를 계산하는 함수
    private double calculateDistance(double userLat, double userLng, double postLat, double postLng) {
        double earthRadius = 6371; // 지구 반지름 (단위: 킬로미터)

        // 사용자 위치
        double lat1 = Math.toRadians(userLat);
        double lng1 = Math.toRadians(userLng);

        // 게시물 위치
        double lat2 = Math.toRadians(postLat);
        double lng2 = Math.toRadians(postLng);

        // 위도 차이와 경도 차이 계산
        double latDiff = lat2 - lat1;
        double lngDiff = lng2 - lng1;

        // 해버사인 공식을 사용하여 거리 계산
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }


}
