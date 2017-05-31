package com.jhzy.receptionevaluation.ui.fragment.elderInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.assess.WebActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

/**
 * 评估
 */
@SuppressLint("SetJavaScriptEnabled")
public class PingguFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String SUBURL = "Evaluations/Index?";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;
    private WebView webView;
    private String url = "";
    private ProgressBar progressBar;


    public PingguFragment() {
        // Required empty public constructor
    }


    public static PingguFragment newInstance(String param1, String param2) {
        PingguFragment fragment = new PingguFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pinggu, container, false);
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        webView = (WebView) view.findViewById(R.id.pinggu_web);
        progressBar = ((ProgressBar) view.findViewById(R.id.pinggu_progress));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "service");
        url = HttpUtils.BASEURL + SUBURL + "elderId=" + id + "&orgAcc=" + SPUtils.find(CONTACT.ACCOUNT);
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(url);
        Log.e("---------", "turn: "+url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("---------", "turn: "+url);
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                //页面下载完毕,却不代表页面渲染完毕显示出来
                //WebChromeClient中progress==100时也是一样
                if (webView.getContentHeight() != 0) {
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("---------", "turn: "+url);
                // TODO Auto-generated method stub
                //自身加载新链接,不做外部跳转
                view.loadUrl(url);
                return true;
            }

        });
    }

    @JavascriptInterface
    public void turn(String url) {
        Log.e("---------", "turn: "+url);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(WebActivity.KEY, url);
        startActivity(intent);
    }
}
