package com.boardinglabs.mireta.standalone.modul.old.baznas.donate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyDonateAdapter;

/**
 * Created by Dhimas on 11/3/17.
 */

public class DonateActivity extends BaseActivity {
    private RecyclerView listDonate;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.donate_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Badan Amil Zakat Nasional");
    }

    @Override
    protected void onCreateAtChild() {
        listDonate = (RecyclerView) findViewById(R.id.donate_list);
        RecyDonateAdapter adapter = new RecyDonateAdapter();
        listDonate.setAdapter(adapter);
        listDonate.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
