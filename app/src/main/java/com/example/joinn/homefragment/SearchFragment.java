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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private EditText departureEditText;
    private EditText destinationEditText;
    private Button confirmButton;
    private GoogleMap mMap;
    private Polyline routePolyline;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        departureEditText = view.findViewById(R.id.departureEditText);
        destinationEditText = view.findViewById(R.id.destinationEditText);
        confirmButton = view.findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departure = departureEditText.getText().toString();
                String destination = destinationEditText.getText().toString();
                // 출발지와 목적지를 이용하여 경로를 처리하는 코드를 추가하세요.
                calculateRoute(departure, destination);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }
    private LatLng convertAddressToLatLng(String address) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList;
        LatLng latLng = null;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (!addressList.isEmpty()) {
                Address location = addressList.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }else{
                //주소를 찾지 못한경우
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;
    }

    private void calculateRoute(String departure, String destination) {
        if (TextUtils.isEmpty(departure) || TextUtils.isEmpty(destination)) {
            // 출발지 또는 목적지가 비어있는 경우 처리할 내용을 여기에 추가하세요.
            return;
        }
        String route = departure + " -> " + destination;

        // 출발지와 목적지 좌표로 변환하여 drawRoute 메서드에 전달
        LatLng departureLatLng = convertAddressToLatLng(departure);
        LatLng destinationLatLng = convertAddressToLatLng(destination);
        if (departureLatLng == null || destinationLatLng == null) {
            // 좌표를 찾을 수 없는 경우 처리할 내용을 여기에 추가하세요.
            return;
        }
        // Directions API 요청을 생성하고 실행
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDoZiB_HmXnhyP1fDfhHY2e6J8frs9Ue2I")
                .build();
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC)
                .origin(new com.google.maps.model.LatLng(departureLatLng.latitude, departureLatLng.longitude))
                .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude));

        try {
            DirectionsResult result = request.await();
            if (result.routes.length > 0) {
                // 경로가 반환되었을 때 처리할 내용을 여기에 추가하세요.
                // 예를 들어, 경로 정보를 가져와서 drawRoute 메서드에 전달할 수 있습니다.
                String polyline = result.routes[0].overviewPolyline.getEncodedPath();
                drawRoute(polyline);
            } else {
                // 경로가 없는 경우 처리할 내용을 여기에 추가하세요.
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 경로 계산 중에 예외가 발생한 경우 처리할 내용을 여기에 추가
            // 경로를 Google 지도에 그리고 표시하는 코드를 작성하세요.
//        drawRoute(departureLatLng, destinationLatLng);
        }
    }

    private void drawRoute(String polyline) {
        // PolylineOptions 대신에 Directions API에서 반환된 polyline을 사용하여 경로를 그립니다.
        List<LatLng> decodedPath = PolyUtil.decode(polyline);

        // PolylineOptions 생성 및 설정
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(decodedPath);
        polylineOptions.width(8f);
        polylineOptions.color(Color.BLUE);

        // Polyline 추가하여 지도에 경로 표시
        if (mMap != null) {
            if (routePolyline != null) {
                routePolyline.remove(); // 기존 경로 제거
            }
            routePolyline = mMap.addPolyline(polylineOptions);

            // 경로가 표시되는 영역에 맞추어 카메라 이동
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng point : decodedPath) {
                boundsBuilder.include(point);
            }
            LatLngBounds bounds = boundsBuilder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}