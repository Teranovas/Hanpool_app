package com.example.joinn.mapfragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joinn.R;
import com.example.joinn.mypagefragment.DriverRegistrationFragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment {

    private MapView mapView;
    private EditText mEditTextLocation;
    private Button locationBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the EditText view for entering the location
        mEditTextLocation = view.findViewById(R.id.location_edit_text);

        // Get the MapView and initialize it
        mapView = view.findViewById(R.id.map_container);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.194002, 127.023045), true); // 초기 위치를 서울로 설정
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

                // AddSearchFragment에서 결과를 받기 위한 함수를 실행
                // 이거 주석풀면 값을 받아와야하는데 버튼누르면 앱이 꺼짐..
//                getSearchResult.launch(new Intent(getActivity(), AddSearchFragment.class));
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

                // Add a marker on the map
                mapView.removeAllPOIItems(); // 기존의 마커들을 모두 제거합니다.
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(location);
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 마커의 색상을 파란색으로 설정합니다.
                mapView.addPOIItem(marker);
            }
        });

        return view;
    }
    //AddSearchFragment에서 주소를 가져와 여기서 처리.
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // AddSearchFragment로부터의 결과값이 이곳으로 전달된다.(setResult에 의해)
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        mEditTextLocation.setText(data);
                    }
                }
            }
    );

}
