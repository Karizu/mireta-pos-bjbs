package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import android.support.v4.app.FragmentTransaction;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;

/**
 * Created by Dhimas on 2/1/18.
 */

public class AllTransactionActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.all_transaction_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Riwayat Transaksi");
    }

    @Override
    protected void onCreateAtChild() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_home, new AllTransactionFragment().newInstance());
        ft.commit();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
