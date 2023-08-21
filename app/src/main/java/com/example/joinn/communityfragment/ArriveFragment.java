package com.example.joinn.communityfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.joinn.R;


public class ArriveFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_arrive, container, false);

        WebView webview = rootView.findViewById(R.id.webView2);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new ArriveFragment.BridgeInterface(), "Android");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Android->Javascript 함수 호출
                webview.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        // 최초 웹뷰 로드
        webview.loadUrl("https://test1-84d99.web.app");

        return rootView;

    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            //카카오 주소 검색 API의 결과값이 브릿지 통로를 통해 전달받는다.(from Javascript)

            String addressData = data;

            // 주소를 이용하여 위도와 경도를 얻어옴
            double[] coordinates = getCoordinatesFromAddress(addressData);

            Bundle bundle = getArguments();
            bundle.putString("arrivedata",data);
            bundle.putDouble("arriveLatitude", coordinates[0]);
            bundle.putDouble("arriveLongitude", coordinates[1]);

            Fragment newFragment = new ArriveRegisterFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            newFragment.setArguments(bundle);
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }
    private double[] getCoordinatesFromAddress(String address) {
        double[] coordinates = new double[2];

        // 주소를 이용하여 위도와 경도를 얻어오는 작업
        // 이 부분은 이전에 설명한 Geocoding API 사용 방법과 비슷합니다.

        return coordinates;
    }
}
