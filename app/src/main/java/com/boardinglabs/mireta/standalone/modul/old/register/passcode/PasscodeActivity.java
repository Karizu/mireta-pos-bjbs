package com.boardinglabs.mireta.standalone.modul.old.register.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;

/**
 * Created by Dhimas on 10/5/17.
 */

public class PasscodeActivity extends AppCompatActivity implements EnterPasscodeFragment.OnCompleteInput,
        ReenterPasscodeFragment.OnCompleteReenterCode, CommonInterface, PasscodeView{
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private PasscodePresenter mPresenter;
    private boolean isForgot;
    private boolean isRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activity);
        isForgot = getIntent().getBooleanExtra(OtpActivity.IS_FORGOT, false);
        isRegister = getIntent().getBooleanExtra(OtpActivity.IS_REGISTER, false);
        mPresenter = new PasscodePresenterImpl(this, this);

        setEnterPasscode();
    }

    public void popFragmentOnTop(){
        getSupportFragmentManager().popBackStack();
    }

    private void makeFragmentTransaction(Fragment aFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_fragment, aFragment).addToBackStack(null);
        ft.commit();
    }

    private void setEnterPasscode() {
        EnterPasscodeFragment fragment = new EnterPasscodeFragment().newInstance(isForgot,isRegister);
        fragment.setOnCompleteListener(this);
        makeFragmentTransaction(fragment);
    }

    private void setReenterPasscode(String code) {
        ReenterPasscodeFragment fragment = new ReenterPasscodeFragment().newInstance(code, isForgot);
        fragment.setReenterCodeListener(this);
        makeFragmentTransaction(fragment);
    }

    @Override
    public void setCode(String code) {
        setReenterPasscode(code);
    }

    @Override
    public void onMatchCode(String code) {
        mPresenter.setPasscode(code, code);
    }

    @Override
    public void onNotMatchCode() {
        setEnterPasscode();
        MethodUtil.showCustomToast(this, "Passcode tidak sesuai", R.drawable.ic_error_login);
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
    }

    @Override
    public void onSuccessResponse() {
        if(isForgot || isRegister){
            PreferenceManager.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


}
