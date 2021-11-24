package com.boardinglabs.mireta.standalone.modul.old.faq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.util.CustomWebChromeClient;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;

/**
 * Created by Dhimas on 1/9/18.
 */

public class FaqActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressBar mWebLoading;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.faq_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebLoading = (ProgressBar) findViewById(R.id.web_loading);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new CustomWebChromeClient(mWebLoading));
        mWebView.setWebViewClient(new CustomWebViewClient(this));
    }

    @Override
    protected void onCreateAtChild() {
        setToolbarTitle("FAQ");
        if (PreferenceManager.getStatusAkupay()) {
            mWebView.loadUrl("file:///android_asset/faq.html");
        } else {
            mWebView.loadUrl("file:///android_asset/faq_agent.html");
        }

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private static class CustomWebViewClient extends WebViewClient {

        private final Activity mActivity;

        CustomWebViewClient(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            MethodUtil.showCustomToast(mActivity, description, R.drawable.ic_error_login);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage("Apakah anda ingin melihat FAQ?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
