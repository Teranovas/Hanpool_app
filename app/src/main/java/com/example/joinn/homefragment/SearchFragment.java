package com.example.joinn.homefragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joinn.R;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.example.joinn.mapfragment.MapFragment;
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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        WebView webview = view.findViewById(R.id.webView);




        String Url = "https://map.kakao.com/link/to/카카오판교오피스,37.402056,127.108212";

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(Url);
        webview.addJavascriptInterface(new SearchFragment.BridgeInterface(), "Android");
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // Android->Javascript 함수 호출
//                webview.loadUrl(Url);
//            }
//        });

        // 최초 웹뷰 로드
//        webview.loadUrl("https://test1-84d99.web.app");

        return view;





//                webview.addJavascriptInterface(getContext().BridgeInterface(), "Android");
        //        mapView = view.findViewById(R.id.search_container);
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.194002, 127.023045), true); // 초기 위치를 서울로 설정
//        mapView.setZoomLevel(0, true); // 초기 줌 레벨 설정, 낮을수록 고도 낮게
//

//                webview.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        // Android->Javascript 함수 호출
//                        webview.loadUrl(Url);
//                    }
//                });


    }


    // 출발지와 목적지를 이용하여 경로를 처리하는 코드
//    private void searchAndDisplayLocation(String departureAddress, String destinationAddress) {
//        Geocoder geocoder = new Geocoder(requireContext());
//
//        try {
//            // 출발지 주소로부터 좌표 검색
//            List<Address> departureAddresses = geocoder.getFromLocationName(departureAddress, 1);
//            if (!departureAddresses.isEmpty()) {
//                Address departureAddressObj = departureAddresses.get(0);
//                MapPoint departurePoint = MapPoint.mapPointWithGeoCoord(
//                        departureAddressObj.getLatitude(), departureAddressObj.getLongitude()
//                );
//
//                // 출발지 마커 추가
//                MapPOIItem departureMarker = createMarker(departurePoint, "Departure");
//                mapView.addPOIItem(departureMarker);
//
//                // 목적지 주소로부터 좌표 검색
//                List<Address> destinationAddresses = geocoder.getFromLocationName(destinationAddress, 1);
//                if (!destinationAddresses.isEmpty()) {
//                    Address destinationAddressObj = destinationAddresses.get(0);
//                    MapPoint destinationPoint = MapPoint.mapPointWithGeoCoord(
//                            destinationAddressObj.getLatitude(), destinationAddressObj.getLongitude()
//                    );
//
//                    // 목적지 마커 추가
//                    MapPOIItem destinationMarker = createMarker(destinationPoint, "Destination");
//                    mapView.addPOIItem(destinationMarker);
//
//                    // 경로 표시 함수 호출
//                    calculateAndDisplayRoute(departurePoint, destinationPoint);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            //카카오 주소 검색 API의 결과값이 브릿지 통로를 통해 전달받는다.(from Javascript)

//            Bundle bundle = new Bundle();
//            bundle.putString("data",data);
//            Fragment newFragment = new MapFragment();
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            newFragment.setArguments(bundle);
//            transaction.replace(R.id.container, newFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();

        }
    }

    //출발지와 도착지에 마커추가하기
//    private MapPOIItem createMarker(MapPoint mapPoint, String title) {
//        MapPOIItem marker = new MapPOIItem();
//        marker.setItemName(title);
//        marker.setTag(0);
//        marker.setMapPoint(mapPoint);
//        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
//        return marker;
//    }
//
//
//    private void calculateAndDisplayRoute(MapPoint departurePoint, MapPoint destinationPoint) {
//
//
//
//    }
}