package com.boardinglabs.mireta.standalone.modul.old.account;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.passcode.PasscodePresenter;
import com.boardinglabs.mireta.standalone.modul.old.register.passcode.PasscodePresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.register.passcode.PasscodeView;

public class ChangePasswordActivity extends BaseActivity implements CommonInterface,PasscodeView {
    private boolean isPINTrue = false;

    private Context c;
    private Button submitButton;
    private PasscodePresenter passcodePresenter;

    private EditText oldPin;
    private EditText newPin;
    private EditText newPinConfirm;

    private String mobile;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Ganti Pin");
    }

    @Override
    protected void onCreateAtChild() {
        c = this;
        mobile = getIntent().getStringExtra(OtpActivity.MOBILE);
        oldPin = findViewById(R.id.old_pin);
        newPin = findViewById(R.id.new_pin);
        newPinConfirm = findViewById(R.id.new_pin_confirmation);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setVisibility(View.INVISIBLE);
        passcodePresenter = new PasscodePresenterImpl(this, this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPin.getText().toString().length()==6 && newPin.getText().toString().length()==6 && newPinConfirm.getText().toString().length()==6) {
                    GAgent user = PreferenceManager.getAgent();
                	//mPresenter.login(user.mobile, oldPin.getText().toString(), PreferenceManager.getImei());
                }else{
                    Toast.makeText(c,"Isi Semua field dengan 6 digit PIN",Toast.LENGTH_LONG).show();
                }

            }
        });

        newPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setVisibility(charSequence.length()>0?View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newPinConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setVisibility(charSequence.length()>0?View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

//    @Override
    public void onSuccessRequest() {
        passcodePresenter.setPasscode(newPin.getText().toString(),newPinConfirm.getText().toString());
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
        Toast.makeText(c,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessResponse() {
        Toast.makeText(c,"Update Password Success",Toast.LENGTH_LONG).show();
        finish();
    }
}
