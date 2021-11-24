package com.boardinglabs.mireta.standalone.modul.old.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.old.onboard.OnBoardActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 10/6/17.
 */

public class RegisterActivity extends AppCompatActivity implements RegisterView{
    private RegisterPresenter mPresenter;
    private EditText textName;
    private EditText textMobile;
    private EditText textEmail;
    private EditText textRefferal;
    private Button registerBtn;
    private TextView backText;
    private String refferalId;
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private ImageView registerImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        textName = (EditText) findViewById(R.id.register_name);
        textMobile = (EditText) findViewById(R.id.register_mobile);
        textEmail = (EditText) findViewById(R.id.register_email);
        textRefferal = (EditText) findViewById(R.id.register_referral);
        registerBtn = (Button) findViewById(R.id.process_register);
        registerImg = (ImageView) findViewById(R.id.register_image);
        backText = (TextView) findViewById(R.id.back_from_register);

//        if (!PreferenceManager.getStatusAkupay()) {
//            registerImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }

        mPresenter = new RegisterPresenterImpl(this);

        RxView.clicks(registerBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (true){//TextUtils.isEmpty(textRefferal.getText().toString())) {
                    refferalId = textRefferal.getText().toString();
                    PreferenceManager.setRefferalId(refferalId);
                    registerRequest();
                } else {
                    mPresenter.checkRefferalId(textRefferal.getText().toString());
                }
            }
        });
        RxView.clicks(backText).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });

        loadOnBoard();
    }

    private void loadOnBoard() {
        Intent intent = new Intent(this, OnBoardActivity.class);
        startActivity(intent);
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onSuccessRegister(String mobile) {
        Toast.makeText(getApplicationContext(), mobile, Toast.LENGTH_SHORT).show();
        gotoOtpActivity();
    }

    @Override
    public void onFailedRegister(String msg) {
        if (msg.equalsIgnoreCase(Constant.ERROR_ALREADY_REGISTERED)) {
            mPresenter.sendCode("0" + textMobile.getText().toString());
        } else {
            MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        }
    }

    @Override
    public void onSuccessResendCode(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        gotoOtpActivity();
    }

    @Override
    public void onSuccessCheckReferral(String refferalId) {
        this.refferalId = refferalId;
        PreferenceManager.setRefferalId(refferalId);
        registerRequest();
    }

    @Override
    public void showProgressBar() {
        progressBar.show(this, "Loading", false, null);
    }

    @Override
    public void hideProgressBar() {
        progressBar.getDialog().dismiss();
    }

    private void gotoOtpActivity() {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(OtpActivity.MOBILE, "0" + textMobile.getText().toString());
        startActivity(intent);
    }

    private void registerRequest() {
        mPresenter.register(textName.getText().toString(), "0" + textMobile.getText().toString(),
                textEmail.getText().toString(), refferalId);
    }
}
