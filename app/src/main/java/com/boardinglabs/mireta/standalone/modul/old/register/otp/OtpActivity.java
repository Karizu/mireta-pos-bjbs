package com.boardinglabs.mireta.standalone.modul.old.register.otp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.register.passcode.PasscodeActivity;

import rx.functions.Action1;


/**
 * Created by Dhimas on 10/5/17.
 */

public class OtpActivity extends AppCompatActivity implements CommonInterface, OtpView {
    public static final String MOBILE = "mobile_text";
    public static final String IS_FORGOT = "forgot";
    public static final String IS_REGISTER = "register";

    private TextView countdownText;
    private TextView resendText;
    private static CustomProgressBar progressBar = new CustomProgressBar();
    private OtpPresenter mPresenter;
    private boolean isFinishCountdown = false;
    private boolean isForgot;
    private ImageView otpImg;
    private EditText entryEditText;
    private ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        mPresenter = new OtpPresenterImpl(this, this);
        final String mobile = getIntent().getStringExtra(MOBILE);
        isForgot = getIntent().getBooleanExtra(IS_FORGOT, false);
        entryEditText = (EditText) findViewById(R.id.txt_pin_entry);
        backButton = (ImageView) findViewById(R.id.backButton);
//        entryEditText.setTransformationMethod(new MyPasswordTransformationMethod());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        entryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){
                    mPresenter.verifyAgent(s.toString(), mobile);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(s.length()>0) {
                        entryEditText.setLetterSpacing(1.0f);
                    }else{
                        entryEditText.setLetterSpacing(0.0f);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        countdownText = (TextView) findViewById(R.id.countdown_text);
        resendText = (TextView) findViewById(R.id.resend_text);
        //otpImg = (ImageView) findViewById(R.id.image_otp);

//        if (!PreferenceManager.getStatusAkupay()) {
//            otpImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pasy_agent));
//        }

        final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdownText.setText("Kirim ulang kode dalam : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isFinishCountdown = true;
                countdownText.setText("");
            }
        }.start();

        RxView.clicks(resendText).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isFinishCountdown) {
                    countDownTimer.start();
                    isFinishCountdown = false;
                    mPresenter.resendCode(mobile);
                }
            }
        });

    }



    @Override
    public void showProgressLoading() {
        progressBar.show(this, "Loading", false, null);
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
    public void onSuccessRequest() {
        Intent intent = new Intent(this, PasscodeActivity.class);
        intent.putExtra(IS_FORGOT, isForgot);
        intent.putExtra(IS_REGISTER,true);
        startActivity(intent);
    }

    @Override
    public void onSuccessResendCode(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
