package com.android.vf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebActivity extends Activity {
    private WebView webView;

    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//启用js
        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        //5.0以下的系统不加这句话,图片来源使用http
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.d("当前url是",url);
        if(url!=null){
            webView.loadUrl(url);
        }
    }
}
