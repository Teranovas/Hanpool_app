package com.example.joinn.mapfragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joinn.R;

public class AddSearchFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search, container, false);

        WebView webview = rootView.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new BridgeInterface(), "Android");
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
//            Intent intent = new Intent();
//            intent.putExtra("data", data);
//            getActivity().setResult(RESULT_OK, intent);
//            getActivity().getSupportFragmentManager().popBackStack(); // AddSearchFragment를 스택에서

            Bundle bundle = new Bundle();
            bundle.putString("data",data);
            Fragment newFragment = new MapFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            newFragment.setArguments(bundle);
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

}
