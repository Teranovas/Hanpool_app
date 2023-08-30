package com.example.joinn.mapfragment;

import static android.content.ContentValues.TAG;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.joinn.R;
import com.example.joinn.communityfragment.DetailFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment {

    private MapView mapView;
    private EditText mEditTextLocation;
    private Button locationBtn;

    private String data;

    private DatabaseReference postsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the EditText view for entering the location
        mEditTextLocation = view.findViewById(R.id.location_edit_text);

        // Get the MapView and initialize it
        mapView = view.findViewById(R.id.map_container);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.194002, 127.023045), true); // 초기 위치를 한신대로 설정
        mapView.setZoomLevel(0, true); // 초기 줌 레벨 설정, 낮을수록 고도 낮게

        locationBtn = view.findViewById(R.id.location_search_btn);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new AddSearchFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Get the button view and set the click listener
        Button button = view.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the location entered by the user
                String location = mEditTextLocation.getText().toString().trim();

                if (location.isEmpty()) {
                    // EditText is empty, show an error message or return
                    return;
                }

                // Convert the location to latitude and longitude using Geocoder
                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList == null || addressList.isEmpty()) {
                    // No address found, show an error message or return
                    return;
                }

                // Get the latitude and longitude from the first address in the list
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Move the camera to the location
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                mapView.setMapCenterPoint(mapPoint, true);
            }
        });

        if (getArguments() != null) {
            data = getArguments().getString("data");
            mEditTextLocation.setText(data);
        }

        // KakaoMap API 초기화
        mapView.setMapViewEventListener(new MapView.MapViewEventListener() {
            @Override
            public void onMapViewInitialized(MapView mapView) {
                // Firebase에서 게시물 데이터 가져오기
                postsRef = FirebaseDatabase.getInstance().getReference("posts");
                postsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String startpoint = postSnapshot.child("startpoint").getValue(String.class);
                            String nickname = postSnapshot.child("writer").getValue(String.class);
                            String title = postSnapshot.child("title").getValue(String.class);
                            String imageUrl = postSnapshot.child("imageUrl").getValue(String.class);
                            String postId = postSnapshot.getKey();

                            // Geocoder 사용하여 주소를 위도, 경도로 변환
                            Geocoder geocoder = new Geocoder(requireContext());
                            List<Address> addressList = null;
                            try {
                                addressList = geocoder.getFromLocationName(startpoint, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addressList != null && !addressList.isEmpty()) {
                                Address address = addressList.get(0);
                                double latitude = address.getLatitude();
                                double longitude = address.getLongitude();

                                // Kakao 지도에 마커 추가
                                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                                MapPOIItem marker = new MapPOIItem();
                                marker.setItemName(title);
                                marker.setTag(0);
                                marker.setMapPoint(mapPoint);
                                marker.setMarkerType(MapPOIItem.MarkerType.RedPin);

                                mapView.addPOIItem(marker);

                                mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
                                    @Override
                                    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {



                                        String arrivepoint = postSnapshot.child("arrivepoint").getValue(String.class);
                                        Long timestamp =  postSnapshot.child("timestamp").getValue(Long.class);

                                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                        DetailFragment detailFragment = new DetailFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title", title);
                                        bundle.putString("start", startpoint);
                                        bundle.putString("arrive", arrivepoint);
                                        bundle.putString("writer", nickname);
                                        bundle.putLong("timestamp", timestamp);
                                        bundle.putString("image", imageUrl);// 작성일 정보 전달
                                        detailFragment.setArguments(bundle);
                                        transaction.replace(R.id.container, detailFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }

                                    @Override
                                    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

                                        Log.d(TAG,"안녕하세요");


                                    }

                                    @Override
                                    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

                                    }

                                    @Override
                                    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 처리 중 에러 발생 시의 동작
                    }
                });
            }

            @Override
            public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {}
            @Override
            public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {}
            @Override
            public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {}
        });

//        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
//            @Override
//            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
//                String itemName = mapPOIItem.getItemName();
//                // 예시로 토스트 메시지를 띄움
//            }
//
//            @Override
//            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {}
//
//            @Override
//            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem,
//                                                         MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {}
//
//            @Override
//            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {}
//        });

        return view;
    }
}
