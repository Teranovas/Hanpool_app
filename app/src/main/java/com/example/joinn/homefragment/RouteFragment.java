package com.example.joinn.homefragment;

import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joinn.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.skt.tmap.TMapData;
import com.skt.tmap.TMapInfo;
import com.skt.tmap.TMapPoint;
import com.skt.tmap.TMapView;
import com.skt.tmap.overlay.TMapPolyLine;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RouteFragment extends Fragment {

    private TimePicker timePicker;
    private DatePicker datePicker;
    private Button btnSave;

    private String address1;

    private String address2;

    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;


    private DatabaseReference usersRef;


    DatabaseReference routeRef = FirebaseDatabase.getInstance().getReference("route");
    DatabaseReference similarRoutesRef = FirebaseDatabase.getInstance().getReference("similarRoutes");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);

        LinearLayout linearLayoutTmap = rootView.findViewById(R.id.tmap);
        TMapView tMapView = new TMapView(getContext());

        timePicker = rootView.findViewById(R.id.timePicker);
        datePicker = rootView.findViewById(R.id.datePicker);
        btnSave = rootView.findViewById(R.id.btnSave);

        Bundle arguments = getArguments();
        address1 = arguments.getString("start");
        address2 = arguments.getString("arrive");

        linearLayoutTmap.addView(tMapView);
        tMapView.setSKTMapApiKey("DXAHGOo2dXantyv0rMg371Lj8Bm8WzV7bhXjJHkh");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String currentUserName = currentUser.getUid();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng coordinatesStart = getLatLngFromAddress(address1);
                LatLng coordinatesEnd = getLatLngFromAddress(address2);
                if (coordinatesStart != null && coordinatesEnd != null) {
                    // Save the user's route to the 'routes' reference
                    DatabaseReference currentUserRouteRef = routeRef.child(FirebaseAuth.getInstance().getUid());

                    usersRef.child(currentUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            HashMap<Object, String> hashMap = new HashMap<>();

                            String nickname = snapshot.child("닉네임").getValue(String.class);



                            hashMap.put("닉네임", nickname);


                            currentUserRouteRef.setValue(hashMap);
                            // Use nested maps to represent the hierarchy
                            Map<String, Object> startMap = new HashMap<>();
                            startMap.put("latitude", coordinatesStart.latitude);
                            startMap.put("longitude", coordinatesStart.longitude);
                            startMap.put("address", address1);

                            Map<String, Object> endMap = new HashMap<>();
                            endMap.put("latitude", coordinatesEnd.latitude);
                            endMap.put("longitude", coordinatesEnd.longitude);
                            endMap.put("address", address2);

                            Map<String, Object> routeMap = new HashMap<>();
                            routeMap.put("start", startMap);
                            routeMap.put("end", endMap);

                            currentUserRouteRef.setValue(routeMap);

                            // Save similar routes to the 'similarRoutes' reference
                            saveSimilarRoutes(coordinatesStart, coordinatesEnd);


                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment newFragment = new HomeFragment();

                            transaction.replace(R.id.container, newFragment);

                            transaction.addToBackStack(null);
                            transaction.commit();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "주소의 좌표를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


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


    private double euclideanDistance(LatLng point1, LatLng point2) {
        return Math.sqrt(Math.pow(point2.latitude - point1.latitude, 2) + Math.pow(point2.longitude - point1.longitude, 2));
    }

    private void saveSimilarRoutes(LatLng currentStart, LatLng currentEnd) {
        routeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map.Entry<String, Double>> sortedRoutes = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) { // Exclude the current user's route
                        LatLng start = new LatLng((double) snapshot.child("start").child("latitude").getValue(), (double) snapshot.child("start").child("longitude").getValue());
                        LatLng end = new LatLng((double) snapshot.child("end").child("latitude").getValue(), (double) snapshot.child("end").child("longitude").getValue());

                        double startDistance = euclideanDistance(currentStart, start);
                        double endDistance = euclideanDistance(currentEnd, end);

                        double totalDistance = startDistance + endDistance;

                        sortedRoutes.add(new AbstractMap.SimpleEntry<>(snapshot.getKey(), totalDistance));
                    }
                }

                // Sort by distance
                sortedRoutes.sort(Map.Entry.comparingByValue());

                // Save to Firebase under the 'similarRoutes' reference for the current user UID
                DatabaseReference userRef = similarRoutesRef.child(FirebaseAuth.getInstance().getUid());

                int count = 0; // This is to ensure only 5 entries are saved
                for (Map.Entry<String, Double> entry : sortedRoutes) {
                    if (count >= 5) break; // If 5 entries are already saved, break out of the loop

                    // Fetch the corresponding route data
                    DataSnapshot routeSnapshot = dataSnapshot.child(entry.getKey());

                    // Save the route data in similarRoutes
                    userRef.child(entry.getKey()).setValue(routeSnapshot.getValue());

                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }





}