package com.jhzy.receptionevaluation.ui.assess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    public static final String KEY = "url";
    public static final String TITLE = "title";


    private WebView web;
    private String url;
    private String title;
    private ImageView back;
    private TextView titleView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override public int getContentView() {
        return R.layout.activity_web;
    }


    @Override public void init() {
        initData();
        web = ((WebView) findViewById(R.id.web_web));
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(this, "service");
        back = ((ImageView) findViewById(R.id.web_back));
        titleView = ((TextView) findViewById(R.id.web_title));
        //titleView.setText(title);
        Log.e("---------", "turn: "+url);
        back.setOnClickListener(new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                finish();
            }
        });
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String url){
                Log.e("---------", "turn: "+url);
                //在这解析这个url的参数，符合要求就跳转页面
                //不符合就调下面方法
                view.loadUrl(url);
                return false;

            }
        });
    }




    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra(KEY);
        //title = intent.getStringExtra(TITLE);
    }

    @JavascriptInterface
    public void tback() {
        finish();
    }

    @JavascriptInterface
    public void turn(String url) {
        Log.e("---------", "turn: "+url);
        web.loadUrl(url);
    }

}
