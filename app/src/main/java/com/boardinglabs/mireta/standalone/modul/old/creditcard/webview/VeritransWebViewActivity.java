package com.boardinglabs.mireta.standalone.modul.old.creditcard.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boardinglabs.mireta.standalone.R;

/**
 * Created by Dhimas on 2/4/18.
 */

public class VeritransWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        String redirectUrl = getIntent().getStringExtra("redirectUrl");
        WebView webView = (WebView) findViewById(R.id.veritrans_webview);
        webView.setWebViewClient(new VeritransWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(redirectUrl);
    }

    // Setup a custom `WebViewClient` to capture completed 3D secure authorization
    class VeritransWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Check if redirect URL has "/token/callback/" to ensure authorization was completed
            if (url.contains("/token/callback/")) {
                // Authorization was completed
                // Handle the charging here
                String[] urlString = url.split("/");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("cardToken", urlString[urlString.length - 1]);
//                resultIntent.putExtra("name", getIntent().getStringExtra("cardName"));
//                resultIntent.putExtra("digit", getIntent().getStringExtra("lastDigit"));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
