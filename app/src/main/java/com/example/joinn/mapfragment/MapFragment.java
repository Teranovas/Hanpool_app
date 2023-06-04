package com.example.joinn.mapfragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joinn.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText mEditTextLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the EditText view for entering the location
        mEditTextLocation = view.findViewById(R.id.location_edit_text);

        // Get the MapView and request for the map asynchronously
        MapView mapView = view.findViewById(R.id.map_container);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

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

                // Call onMapReady to show the location on the map
                onMapReady(mMap);
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get the location entered by the user
        String location = mEditTextLocation.getText().toString().trim();

        if (location.isEmpty()) {
            // EditText is empty, show an error message or return
            return;
        }

        // Create a Geocoder object to convert the location into latitude and longitude
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.isEmpty()) {
            // No address found, show an error message or return
            return;
        }

        // Get the latitude and longitude from the first address in the list
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        // Add a marker on the map
        mMap.addMarker(new MarkerOptions().position(latLng).title(location));

        // Move the camera to the location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}

