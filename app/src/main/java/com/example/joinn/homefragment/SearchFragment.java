package com.example.joinn.homefragment;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.joinn.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{

    private EditText departureEditText;
    private EditText destinationEditText;
    private Button confirmButton;
//    private GoogleMap mMap;
    private MapView mapView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        departureEditText = view.findViewById(R.id.departureEditText);
        destinationEditText = view.findViewById(R.id.destinationEditText);
        confirmButton = view.findViewById(R.id.confirmButton);

        mapView = view.findViewById(R.id.search_container);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.194002, 127.023045), true); // 초기 위치를 서울로 설정
        mapView.setZoomLevel(0, true); // 초기 줌 레벨 설정, 낮을수록 고도 낮게


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departure = departureEditText.getText().toString();
                String destination = destinationEditText.getText().toString();

                searchAndDisplayLocation(departure, destination);
            }
        });

        return view;
    }

    // 출발지와 목적지를 이용하여 경로를 처리하는 코드
    private void searchAndDisplayLocation(String departureAddress, String destinationAddress) {
        Geocoder geocoder = new Geocoder(requireContext());

        try {
            // 출발지 주소로부터 좌표 검색
            List<Address> departureAddresses = geocoder.getFromLocationName(departureAddress, 1);
            if (!departureAddresses.isEmpty()) {
                Address departureAddressObj = departureAddresses.get(0);
                MapPoint departurePoint = MapPoint.mapPointWithGeoCoord(
                        departureAddressObj.getLatitude(), departureAddressObj.getLongitude()
                );

                // 출발지 마커 추가
                MapPOIItem departureMarker = createMarker(departurePoint, "Departure");
                mapView.addPOIItem(departureMarker);

                // 목적지 주소로부터 좌표 검색
                List<Address> destinationAddresses = geocoder.getFromLocationName(destinationAddress, 1);
                if (!destinationAddresses.isEmpty()) {
                    Address destinationAddressObj = destinationAddresses.get(0);
                    MapPoint destinationPoint = MapPoint.mapPointWithGeoCoord(
                            destinationAddressObj.getLatitude(), destinationAddressObj.getLongitude()
                    );

                    // 목적지 마커 추가
                    MapPOIItem destinationMarker = createMarker(destinationPoint, "Destination");
                    mapView.addPOIItem(destinationMarker);

                    // 경로 표시 함수 호출
                    calculateAndDisplayRoute(departurePoint, destinationPoint);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //출발지와 도착지에 마커추가하기
    private MapPOIItem createMarker(MapPoint mapPoint, String title) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(title);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        return marker;
    }


    private void calculateAndDisplayRoute(MapPoint departurePoint, MapPoint destinationPoint) {



    }
}