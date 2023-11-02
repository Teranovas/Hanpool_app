package com.example.joinn.homefragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joinn.chatfragment.ChatFragment;
import com.example.joinn.chatfragment.User;
import com.example.joinn.homefragment.Route;
import com.example.joinn.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.skt.tmap.overlay.TMapMarkerItem;
import com.skt.tmap.overlay.TMapPolyLine;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;
    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String writer;

    private DatabaseReference usersRef;

    private DatabaseReference chatlistRef;

    private DatabaseReference opponentChatListRef;

    private StorageReference storageRef;

    private DatabaseReference similarRoutesRef;

    private DatabaseReference routesRef;

    String imageURL;




    public RouteAdapter(Context context, List<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);

        chatlistRef = FirebaseDatabase.getInstance().getReference().child("chatlist");
        opponentChatListRef = FirebaseDatabase.getInstance().getReference().child("chatlist");
        storageRef = FirebaseStorage.getInstance().getReference();

        similarRoutesRef = FirebaseDatabase.getInstance().getReference().child("similarRoutes");

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.startTextView.setText(route.getStartAddress());
        holder.endTextView.setText(route.getEndAddress());
        holder.nicknameTextView.setText(route.getNickname());
        holder.spotTextView.setText("" + route.getSpottxt());
        holder.levelTextView.setText("레벨: " + route.getLevel());


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        String nickname = route.getNickname();


        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        routesRef = FirebaseDatabase.getInstance().getReference().child("route");
        TMapView tMapView = new TMapView(context);

        holder.tmapLayout.addView(tMapView);
        tMapView.setSKTMapApiKey("DXAHGOo2dXantyv0rMg371Lj8Bm8WzV7bhXjJHkh");


        Log.d(TAG, "이름 : " + nickname);

        tMapView.setOnMapReadyListener(new TMapView.OnMapReadyListener() {
            @Override
            public void onMapReady() {
                usersRef.orderByChild("닉네임").equalTo(nickname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String uid = userSnapshot.getKey(); // 각 사용자의 UID를 가져옵니다.
                            Log.d(TAG, "로그 : " + uid);



                            routesRef.child(uid).child("start").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot startSnapshot) {
                                    if (startSnapshot.exists()) {
                                        String address = startSnapshot.child("address").getValue(String.class);
                                        double latitude = startSnapshot.child("latitude").getValue(Double.class);
                                        double longitude = startSnapshot.child("longitude").getValue(Double.class);

                                        Log.d(TAG, "위치 : " + address);

                                        LatLng coordinatesStart = new LatLng(latitude, longitude);

                                        if (coordinatesStart != null) {
                                            TMapMarkerItem marker = new TMapMarkerItem();
                                            TMapPoint tMapPoint1 = new TMapPoint(coordinatesStart.latitude, coordinatesStart.longitude);




                                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_foreground);

                                            marker.setIcon(bitmap); // 마커 아이콘 지정
                                            marker.setPosition(0.5f, 1.0f);
                                            marker.setTMapPoint( tMapPoint1 );


                                            marker.setName("출발");

                                            tMapView.setCenterPoint(coordinatesStart.latitude, coordinatesStart.longitude);

                                        }
                                    } else {
                                        Log.d(TAG, "에러");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // 오류 처리
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 오류 처리
                    }
                });
            }
        });

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String currentUserName = currentUser.getUid();
                    writer = holder.nicknameTextView.getText().toString();
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
                                                Toast.makeText(context, "신청되었습니다.", Toast.LENGTH_SHORT).show();
                                                User user = new User(postId, userNickname, userImageUrl); // 현재 사용자의 닉네임과 이미지 저장
                                                chatListRef.child(postId).setValue(user); // 상대방의 chatList에 저장

                                                // 상대방도 채팅 목록에 현재 사용자 정보 추가
                                                DatabaseReference opponentChatListRef = FirebaseDatabase.getInstance().getReference().child("chatList").child(currentUserName);
                                                User opponentUser = new User(postId, writer, imageURL); // 상대방의 닉네임과 이미지 저장
                                                opponentChatListRef.child(postId).setValue(opponentUser);

                                                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                                                ChatFragment chatFragment = new ChatFragment();
                                                transaction.replace(R.id.container, chatFragment);
                                                transaction.addToBackStack(null);
                                                transaction.commit();

                                            }
                                            else {
                                                // 현재 로그인한 사용자가 게시물 작성자인 경우 처리할 내용 추가
                                                Toast.makeText(context, "자기 자신한테는 신청할 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView startTextView;
        TextView endTextView;

        TextView nicknameTextView;
        TextView spotTextView;

        ImageView imageView;
        TextView levelTextView;

        Button submitBtn;

        LinearLayout tmapLayout;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            startTextView = itemView.findViewById(R.id.route_start);
            endTextView = itemView.findViewById(R.id.route_end);
            nicknameTextView = itemView.findViewById(R.id.NickNameText);
            spotTextView = itemView.findViewById(R.id.SpotText);

            levelTextView = itemView.findViewById(R.id.LevelText);

            imageView = itemView.findViewById(R.id.ImageText);

            submitBtn = itemView.findViewById(R.id.SummitBtn);

            tmapLayout = itemView.findViewById(R.id.tmap);




            Glide.with(context).load(Route.getImageURL()).into(imageView);
        }
    }

    public LatLng getLatLngFromAddress(String address){
        Geocoder geocoder = new Geocoder(context);
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
