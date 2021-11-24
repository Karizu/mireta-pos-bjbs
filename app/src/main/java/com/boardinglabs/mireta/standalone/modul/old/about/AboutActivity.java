package com.boardinglabs.mireta.standalone.modul.old.about;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;

public class AboutActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle(getResources().getString(R.string.about));
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
