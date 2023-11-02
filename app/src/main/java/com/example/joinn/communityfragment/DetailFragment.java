package com.example.joinn.communityfragment;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.example.joinn.chatfragment.ChatFragment;
import com.example.joinn.chatfragment.ChatRoomFragment;
import com.example.joinn.chatfragment.User;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapInfo;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapPolyLine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView startTextView;
    private TextView arriveTextView;

    private TextView timestampTextView;

    private TextView writerTextView;


    private ImageView detailProfileImageView;

    private String writer;


    private Button submmitBtn;

    private DatabaseReference usersRef;

    private DatabaseReference chatlistRef;

    private DatabaseReference opponentChatListRef;

    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    ArrayList<TMapPoint> pointList = new ArrayList<TMapPoint>();

    private String address1;

    private String address2;




    String imageURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatlistRef = FirebaseDatabase.getInstance().getReference().child("chatlist");
        opponentChatListRef = FirebaseDatabase.getInstance().getReference().child("chatlist");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);


        detailProfileImageView = view.findViewById(R.id.detailProfileImageView);

        titleTextView = view.findViewById(R.id.title_tv);
        startTextView = view.findViewById(R.id.start);
        arriveTextView = view.findViewById(R.id.arrive);
        timestampTextView = view.findViewById(R.id.time);
        writerTextView = view.findViewById(R.id.nickname);
        submmitBtn = view.findViewById(R.id.SummitBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        LinearLayout linearLayoutTmap = view.findViewById(R.id.tmap);
        TMapView tMapView = new TMapView(getContext());

        Bundle arguments = getArguments();

        String title = arguments.getString("title");
        titleTextView.setText(title);

        address1 = arguments.getString("start");
        startTextView.setText(address1);

        address2 = arguments.getString("arrive");
        arriveTextView.setText(address2);

        String timestamp = arguments.getString("timestamp");
        timestampTextView.setText(timestamp);

        writer = arguments.getString("writer");
        writerTextView.setText(writer);

        imageURL = arguments.getString("image");

        Glide.with(this)
                .load(imageURL)
                .into(detailProfileImageView);

        linearLayoutTmap.addView(tMapView);
        tMapView.setSKTMapApiKey("DXAHGOo2dXantyv0rMg371Lj8Bm8WzV7bhXjJHkh");




        tMapView.setOnMapReadyListener(new TMapView.OnMapReadyListener() {
            @Override
            public void onMapReady() {

                LatLng coordinatesStart = getLatLngFromAddress(address1);
                LatLng coordinatesEnd = getLatLngFromAddress(address2);

                if (coordinatesStart != null && coordinatesEnd != null) {
                    TMapPoint startTMapPoint = new TMapPoint(coordinatesStart.latitude, coordinatesStart.longitude);
                    TMapPoint endTMapPoint = new TMapPoint(coordinatesEnd.latitude, coordinatesEnd.longitude);

                    TMapData tmapdata = new TMapData();

                    tmapdata.findPathData(startTMapPoint, endTMapPoint, new TMapData.OnFindPathDataListener() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            tMapPolyLine.setLineWidth(3);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineAlpha(255);

                            tMapPolyLine.setOutLineWidth(5);
                            tMapPolyLine.setOutLineColor(Color.RED);
                            tMapPolyLine.setOutLineAlpha(255);


                            tMapView.addTMapPolyLine(tMapPolyLine);
                            TMapInfo info = tMapView.getDisplayTMapInfo(tMapPolyLine.getLinePointList());
                            tMapView.setZoomLevel(info.getZoom());
                            tMapView.setCenterPoint(info.getPoint().getLatitude(), info.getPoint().getLongitude());

                            tMapView.setZoomLevel(10);

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "주소의 좌표를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }


        });

        tMapView.setOnClickListenerCallback(new TMapView.OnClickListenerCallback() {
            @Override
            public void onPressDown(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                Toast.makeText(getContext(), "onPressDown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPressUp(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                Toast.makeText(getContext(), "onPressUp", Toast.LENGTH_SHORT).show();
            }
        });



        submmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String currentUserName = currentUser.getUid();
                    writer = writerTextView.getText().toString();
                    String postId = chatlistRef.push().getKey();


                    // 현재 로그인한 사용자와 게시물 작성자가 다른 경우에만 채팅 화면으로 이동

                    // 현재 사용자의 UID로 users 레퍼런스에 접근하여 닉네임 가져오기
                    usersRef.child(currentUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // 현재 로그인한 사용자와 게시물 작성자가 다른 경우에만 채팅 화면으로 이동

                            // 현재 사용자의 UID로 users 레퍼런스에 접근하여 닉네임 가져오기
                            if (dataSnapshot.exists()) {
                                String userNickname = dataSnapshot.child("닉네임").getValue(String.class);
                                String userImageUrl = dataSnapshot.child("photoUrl").getValue(String.class);

                                usersRef.orderByChild("닉네임").equalTo(writer).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                            String opponentUID = userSnapshot.getKey(); // 해당 사용자의 UID를 가져옵니다.

                                            // 이제 opponentUID를 사용하여 원하는 작업을 수행할 수 있습니다.
                                            Log.d(TAG, "상대방의 UID: " + opponentUID);

                                            if (!currentUserName.equals(opponentUID)) {



                                                DatabaseReference chatListRef = FirebaseDatabase.getInstance().getReference().child("chatList").child(opponentUID); // 상대방의 chatList 레퍼런스
                                                Toast.makeText(getContext(), "신청하였습니다.", Toast.LENGTH_SHORT).show();
                                                User user = new User(postId, userNickname, userImageUrl); // 현재 사용자의 닉네임과 이미지 저장
                                                chatListRef.child(postId).setValue(user); // 상대방의 chatList에 저장

                                                // 상대방도 채팅 목록에 현재 사용자 정보 추가
                                                DatabaseReference opponentChatListRef = FirebaseDatabase.getInstance().getReference().child("chatList").child(currentUserName);
                                                User opponentUser = new User(postId, writer, imageURL); // 상대방의 닉네임과 이미지 저장
                                                opponentChatListRef.child(postId).setValue(opponentUser);

                                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                                ChatFragment chatFragment = new ChatFragment();
                                                transaction.replace(R.id.container, chatFragment);
                                                transaction.addToBackStack(null);
                                                transaction.commit();

                                            }
                                            else {
                                                // 현재 로그인한 사용자가 게시물 작성자인 경우 처리할 내용 추가
                                                Toast.makeText(getContext(), "자기 자신한테는 신청할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e(TAG, "데이터베이스에서 사용자를 찾는 중 오류 발생", databaseError.toException());
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 처리 중 오류 발생 시
                        }
                    });
                }

            }
        });

        return view;
    }
    //경기 가평군 상면 돌아우길 14

    public LatLng getLatLngFromAddress(String address){
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;
        LatLng p1 = null;

        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if (addresses == null || addresses.size() == 0) {
                return null;
            }
            Address location = addresses.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }



}
