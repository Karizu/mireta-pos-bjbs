package com.boardinglabs.mireta.standalone.modul.old.termcondition;

import android.webkit.WebView;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Dhimas on 12/29/17.
 */

public class TermConditionActivity extends BaseActivity {
    private WebView ketentuan;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.termcondition_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle(getResources().getString(R.string.syarat_ketentuan));
        ketentuan = (WebView) findViewById(R.id.ketentuan);
        InputStream xrp = getResources().openRawResource(R.raw.ketentuan);
        String xmlString;
        try {
            xmlString = isToString(xrp);
            ketentuan.getSettings().setJavaScriptEnabled(true);
            ketentuan.loadData(xmlString, "text/html; charset=utf-8", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String isToString(InputStream is) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
