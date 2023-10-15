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
import com.example.joinn.communityfragment.DetailFragment;
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

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                RouteFragment routeFragment = new RouteFragment();
                Bundle bundle = new Bundle();

                bundle.putString("start", address1);
                bundle.putString("arrive", address2);

                routeFragment.setArguments(bundle);
                transaction.replace(R.id.container, routeFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });





        return rootView;
    }


}



