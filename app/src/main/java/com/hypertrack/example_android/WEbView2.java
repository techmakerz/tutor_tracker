package com.hypertrack.example_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
public class WEbView2 extends AppCompatActivity {
    private WebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        String mainLink="http://hypertrack-com.stackstaging.com/iframe.php?user_id="+link;
        mWebview = (android.webkit.WebView) findViewById(R.id.webview);
        WebSettings s = mWebview.getSettings();
        s.setJavaScriptEnabled(true);
        if (savedInstanceState != null)
            mWebview.restoreState(savedInstanceState);
        else{
            mWebview.loadUrl(mainLink);
        }


        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setUseWideViewPort(true);
    }
}
