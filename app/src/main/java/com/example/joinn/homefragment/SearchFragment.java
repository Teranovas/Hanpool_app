//package com.example.joinn.homefragment;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.EditText;
//import android.webkit.JavascriptInterface;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.joinn.R;
//import com.example.joinn.mapfragment.AddSearchFragment;
//import com.example.joinn.mapfragment.MapFragment;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.maps.DirectionsApi;
//import com.google.maps.DirectionsApiRequest;
//import com.google.maps.GeoApiContext;
//import com.google.maps.android.PolyUtil;
//import com.google.maps.model.DirectionsResult;
//import com.google.maps.model.TravelMode;
//import com.google.maps.model.Unit;
//
//import net.daum.mf.map.api.MapPOIItem;
//import net.daum.mf.map.api.MapPoint;
//import net.daum.mf.map.api.MapView;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class SearchFragment extends Fragment implements OnMapReadyCallback{
//
////    private GoogleMap mMap;
////    ArrayList<LatLng> markerPoints = new ArrayList<>();
////
////    @Nullable
////    @Override
////    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
////
////        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
////        mapFragment.getMapAsync(this);
////
////        return rootView;
////    }
////
////    @Override
////    public void onMapReady(GoogleMap googleMap) {
////        mMap = googleMap;
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
////
////        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////            @Override
////            public void onMapClick(LatLng latLng) {
////                if (markerPoints.size() > 1) {
////                    markerPoints.clear();
////                    mMap.clear();
////                }
////                // Adding new item to the ArrayList
////                markerPoints.add(latLng);
////                // Creating MarkerOptions
////                MarkerOptions options = new MarkerOptions();
////                // Setting the position of the marker
////                options.position(latLng);
////                if (markerPoints.size() == 1) {
////                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
////                } else if (markerPoints.size() == 2) {
////                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
////                }
////
////                // Add new marker to the Google Map Android API V2
////                mMap.addMarker(options);
////
////                if (markerPoints.size() >= 2) {
////                    LatLng origin = (LatLng) markerPoints.get(0);
////                    LatLng dest = (LatLng) markerPoints.get(1);
////                    // Adding new item to the ArrayList
////                    markerPoints.add(latLng);
////                    // Creating MarkerOptions
////                    MarkerOptions options = new MarkerOptions();
////                    // Setting the position of the marker
////                    options.position(latLng);
////                    if (markerPoints.size() == 1) {
////                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
////                    } else if (markerPoints.size() == 2) {
////                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
////                    }
////
////                    // Add new marker to the Google Map Android API V2
////                    mMap.addMarker(options);
////
////                    // Getting URL to the Google Directions API
////                    String url = getDirectionsUrl(origin, dest);
////
////                    DownloadTask downloadTask = new DownloadTask();
////
////                    // Start downloading json data from Google Directions API
////                    downloadTask.execute(url);
////                }
////
////
////            }
////        });
////    }
////    private class DownloadTask extends AsyncTask<String, Void, String> {
////        @Override
////        protected String doInBackground(String... url) {
////
////            String data = "";
////
////            try {
////                data = downloadUrl(url[0]);
////            } catch (Exception e) {
////                Log.d("Background Task", e.toString());
////            }
////            return data;
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            super.onPostExecute(result);
////
////            ParserTask parserTask = new ParserTask();
////
////
////            parserTask.execute(result);
////
////        }
////    }
////
////    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
////
////        // Parsing the data in non-ui thread
////        @Override
////        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
////
////            JSONObject jObject;
////            List<List<HashMap<String, String>>> routes = null;
////
////            try {
////                jObject = new JSONObject(jsonData[0]);
////                DirectionsJSONParser parser = new DirectionsJSONParser();
////
////                routes = parser.parse(jObject);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            return routes;
////        }
////
////        @Override
////        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
////            ArrayList<LatLng> points;
////            PolylineOptions lineOptions = null;
////
////            for (int i = 0; i < result.size(); i++) {
////                points = new ArrayList<>();
////                lineOptions = new PolylineOptions();
////
////                List<HashMap<String, String>> path = result.get(i);
////
////                for (int j = 0; j < path.size(); j++) {
////                    HashMap<String, String> point = path.get(j);
////
////                    double lat = Double.parseDouble(point.get("lat"));
////                    double lng = Double.parseDouble(point.get("lng"));
////
////                    LatLng position = new LatLng(lat, lng);
////                    points.add(position);
////                }
////
////                lineOptions.addAll(points);
////                lineOptions.width(12);
////                lineOptions.color(Color.RED);
////                lineOptions.geodesic(true);
////
////                mMap.addPolyline(lineOptions);
////            }
////        }
////    }
////
////
////    private String getDirectionsUrl(LatLng origin, LatLng dest) {
////// Origin of route
////        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
////
////        // Destination of route
////        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
////
////        // Sensor enabled
////        String sensor = "sensor=false";
////        String mode = "mode=driving";
////
////        // Building the parameters to the web service
////        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
////
////        // Output format
////        String output = "json";
////
////        // Building the url to the web service
////        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
////
////
////        return url;
////    }
////
////    private String downloadUrl(String strUrl) throws IOException {
////        String data = "";
////        InputStream iStream = null;
////        HttpURLConnection urlConnection = null;
////        try {
////            URL url = new URL(strUrl);
////
////            urlConnection = (HttpURLConnection) url.openConnection();
////
////            urlConnection.connect();
////
////            iStream = urlConnection.getInputStream();
////
////            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
////
////            StringBuffer sb = new StringBuffer();
////
////            String line = "";
////            while ((line = br.readLine()) != null) {
////                sb.append(line);
////            }
////
////            data = sb.toString();
////
////            br.close();
////
////        } catch (Exception e) {
////            Log.d("Exception", e.toString());
////        } finally {
////            iStream.close();
////            urlConnection.disconnect();
////        }
////        return data;
////    }
//
//
//
//}