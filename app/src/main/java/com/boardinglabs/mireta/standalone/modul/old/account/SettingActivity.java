package com.boardinglabs.mireta.standalone.modul.old.account;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.RateItDialogFragment;
import com.boardinglabs.mireta.standalone.modul.old.about.AboutActivity;
import com.boardinglabs.mireta.standalone.modul.old.editprofile.EditProfileActivity;
import com.boardinglabs.mireta.standalone.modul.old.termcondition.TermConditionActivity;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 11/3/17.
 */

public class SettingActivity extends BaseActivity {
    private Button ubahAkunButton;
    private Button bantuanButton;
    private Button ketentuanButton;
    private Button keluarButton;
    private Button tentangButton;
    private Button ulasAplikasiButton;
    private Button gantiPinButton;

    private Context mContext;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.setting_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Pengaturan");
    }

    @Override
    protected void onCreateAtChild() {
        mContext = this;
        ubahAkunButton = (Button) findViewById(R.id.ubahAkunButton);
        RxView.clicks(ubahAkunButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

        bantuanButton = (Button) findViewById(R.id.bantuanButton);
        bantuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ZopimChatActivity.class));
            }
        });

        ketentuanButton = (Button) findViewById(R.id.ketentuanButton);
        ketentuanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TermConditionActivity.class));
            }
        });

        keluarButton = (Button) findViewById(R.id.keluarButton);
        keluarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        logout();

            }
        });

        tentangButton = (Button) findViewById(R.id.tentangButton);
        tentangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });

        ulasAplikasiButton = (Button) findViewById(R.id.ulasAplikasiButton);
        ulasAplikasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateDialog();
            }
        });

        gantiPinButton = (Button) findViewById(R.id.gantiPinButton);
        gantiPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                //startActivity(new Intent(getApplicationContext(), ForgotPassActivity.class));
            }
        });
    }

    private void showRateDialog(){
        RateItDialogFragment.show(mContext,getSupportFragmentManager());
    }

    private void logout(){
//        PreferenceManager.logOut();
//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//        goToLoginPage();
        logoutAction(false);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
