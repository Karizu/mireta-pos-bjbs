package com.boardinglabs.mireta.standalone.modul.old.checkpasscode;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 11/29/17.
 */

public class CheckPasscodeActivity extends AppCompatActivity implements CommonInterface {
    public static String TRANSACTION_KEY = "TRANSACTION";
    public static String TRANSACTION_PPOB = "TRANSCATION PPOB";

    private static CustomProgressBar progressBar = new CustomProgressBar();
//    private PasscodeLoginPresenter mPresenter;
    private String mobile;
    private EditText passwordInput;
    private TextView toolbarTitle;
    private ImageView backButton;
    private CircleImageView profileImage;
    private RelativeLayout bottomMenu;
    private TextView hintText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passcode_layout);
        TextView greetingTxt = (TextView) findViewById(R.id.greeting_text);
        greetingTxt.setText("");
//        mPresenter = new PasscodeLoginPresenterImpl(this, this);
        mobile = getIntent().getStringExtra(OtpActivity.MOBILE);
        passwordInput = (EditText) findViewById(R.id.pin_input);
        profileImage = (CircleImageView) findViewById(R.id.profile);
//        passwordInput.setTransformationMethod(new MyPasswordTransformationMethod());
        toolbarTitle = (TextView) findViewById(R.id.hometoolbar_title);
        backButton = (ImageView) findViewById(R.id.backButton);
        bottomMenu = (RelativeLayout) findViewById(R.id.bottom_menu);
        hintText = (TextView) findViewById(R.id.hint_text);

        toolbarTitle.setText("KODE PIN");
        profileImage.setVisibility(View.INVISIBLE);
        bottomMenu.setVisibility(View.INVISIBLE);
        if(getIntent().getStringExtra(TRANSACTION_KEY)==null) {
            hintText.setVisibility(View.GONE);
        }
        else{
            hintText.setVisibility(View.VISIBLE);
            hintText.setText("Ketik kode PIN untuk validasi transaksi.");
        }
//        Log.i("kunam", getIntent().getStringExtra("hinttext"));
        if(getIntent().getStringExtra("hinttext")!=null){
            hintText.setVisibility(View.VISIBLE);
            hintText.setText(getIntent().getStringExtra("hinttext"));
        }else{
            hintText.setVisibility(View.VISIBLE);
            hintText.setText("Ketik kode PIN untuk validasi transaksi.");
        }

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){
//                    mPresenter.login(mobile, passwordInput.getText().toString(), PreferenceManager.getSavedToken());
//					mPresenter.login(mobile, passwordInput.getText().toString(), PreferenceManager.getImei());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(s.length()>0) {
                        passwordInput.setLetterSpacing(1.0f);
                    }else{
                        passwordInput.setLetterSpacing(0.0f);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPresssd();
            }
        });
    }

    private void onBackPresssd(){
        super.onBackPressed();
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
        setResult(RESULT_CANCELED);
        finish();
    }

//    @Override
    public void onSuccessRequest() {
        setResult(RESULT_OK);
        finish();
    }
}
