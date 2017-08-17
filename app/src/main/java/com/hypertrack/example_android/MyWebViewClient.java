package com.hypertrack.example_android;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by anurag on 6/6/2017.
 */


public class MyWebViewClient extends WebViewClient {
//WebView mWebView;
//    MyWebViewClient(WebView mWebView){
//        this.mWebView=mWebView;
//    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d("karma","im inside "+url);
            return shouldOverrideUrlLoading(view, Uri.parse(url));
        }
        Log.d("karma","im outside "+url);

        view.loadUrl(url);
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }


    private boolean shouldOverrideUrlLoading(final WebView view, final Uri request) {
        if(request.getScheme().equals("blob")) {
            // do your special handling for blob urls here
            return true;
        }
        return true;
    }
}
