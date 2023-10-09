package com.example.joinn.homefragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
//import com.example.joinn.Manifest;
import com.example.joinn.R;
import com.example.joinn.mapfragment.AddSearchFragment;
import com.example.joinn.mapfragment.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import com.google.maps.android.PolyUtil;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;



import com.google.android.gms.maps.model.LatLng;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapInfo;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapPolyLine;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





public class SearchFragment extends Fragment{


    private Button searchButton;
    private EditText originEditText;
    private EditText destinationEditText;


    ArrayList<TMapPoint> pointList = new ArrayList<TMapPoint>();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        searchButton = rootView.findViewById(R.id.searchButton);
        originEditText = rootView.findViewById(R.id.originEditText);
        destinationEditText = rootView.findViewById(R.id.destinationEditText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address1 = originEditText.getText().toString();
                String address2 = destinationEditText.getText().toString();

            }
        });


//        LinearLayout linearLayoutTmap = rootView.findViewById(R.id.tmap);
//        TMapView tMapView = new TMapView(getContext());

//        linearLayoutTmap.addView(tMapView);
//        tMapView.setSKTMapApiKey("DXAHGOo2dXantyv0rMg371Lj8Bm8WzV7bhXjJHkh");
//
//        tMapView.setOnMapReadyListener(new TMapView.OnMapReadyListener() {
//            @Override
//            public void onMapReady() {
//                searchButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String address1 = originEditText.getText().toString();
//                        String address2 = destinationEditText.getText().toString();
//
//                        LatLng coordinatesStart = getLatLngFromAddress(address1);
//                        LatLng coordinatesEnd = getLatLngFromAddress(address2);
//
//                        if (coordinatesStart != null && coordinatesEnd != null) {
//                            TMapPoint startTMapPoint = new TMapPoint(coordinatesStart.latitude, coordinatesStart.longitude);
//                            TMapPoint endTMapPoint = new TMapPoint(coordinatesEnd.latitude, coordinatesEnd.longitude);
//
//                            TMapData tmapdata = new TMapData();
//
//                            tmapdata.findPathData(startTMapPoint, endTMapPoint, new TMapData.OnFindPathDataListener() {
//                                @Override
//                                public void onFindPathData(TMapPolyLine tMapPolyLine) {
//                                    tMapPolyLine.setLineWidth(3);
//                                    tMapPolyLine.setLineColor(Color.BLUE);
//                                    tMapPolyLine.setLineAlpha(255);
//
//                                    tMapPolyLine.setOutLineWidth(5);
//                                    tMapPolyLine.setOutLineColor(Color.RED);
//                                    tMapPolyLine.setOutLineAlpha(255);
//
//
//
//
//                                    tMapView.addTMapPolyLine(tMapPolyLine);
//                                    TMapInfo info = tMapView.getDisplayTMapInfo(tMapPolyLine.getLinePointList());
//                                    tMapView.setZoomLevel(info.getZoom());
//                                    tMapView.setCenterPoint(info.getPoint().getLatitude(), info.getPoint().getLongitude());
//
//                                    tMapView.setZoomLevel(10);
//
//                                }
//                            });
//                        } else {
//                            Toast.makeText(getContext(), "주소의 좌표를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//        tMapView.setOnClickListenerCallback(new TMapView.OnClickListenerCallback() {
//            @Override
//            public void onPressDown(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
//                Toast.makeText(getContext(), "onPressDown", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPressUp(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
//                Toast.makeText(getContext(), "onPressUp", Toast.LENGTH_SHORT).show();
//            }
//        });


        return rootView;
    }

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



