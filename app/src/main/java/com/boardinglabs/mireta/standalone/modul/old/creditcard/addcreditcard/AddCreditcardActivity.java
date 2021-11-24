package com.boardinglabs.mireta.standalone.modul.old.creditcard.addcreditcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.TextWatcherAdapter;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardPresenter;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardPresenterImpl;

import org.parceler.apache.commons.lang.StringUtils;

import java.util.Calendar;

import io.card.payment.CardIOActivity;
import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.MY_SCAN_REQUEST_CODE;

/**
 * Created by Dhimas on 2/16/18.
 */

public class AddCreditcardActivity extends BaseActivity implements CommonInterface, AddCreditcardPresenterImpl.EventAddCreditcardPresenter {
    private InputCreditcardPresenter mPresenter;
    private EditText cardNumber;
    private EditText expMonth;
    private EditText expYear;
    private EditText cvv;
    private ImageView scanCard;
    private Button nextBtn;
    private boolean isGrantedUsingCamera;
    private AddCreditcardPresenter aPresenter;
    private CheckBox saveCard;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.add_creditcard_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Tambah Kartu");
        cardNumber = (EditText) findViewById(R.id.no_card);
        expMonth = (EditText) findViewById(R.id.exp_month);
        expYear = (EditText) findViewById(R.id.exp_year);
        cvv = (EditText) findViewById(R.id.cvv);
        scanCard = (ImageView) findViewById(R.id.scan_card);
        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setText("SIMPAN");
        saveCard = (CheckBox) findViewById(R.id.save_card);
        saveCard.setVisibility(View.GONE);
        addTextChangeListener(cardNumber);
        RxView.clicks(scanCard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isGrantedUsingCamera) {
                    onScanPress();
                }
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addCreditCard();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    private void addCreditCard() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month;
        if (expMonth.getText().length() == 2) {
            month = expMonth.getText().toString();
        } else {
            month = "0" + expMonth.getText().toString();
        }
        final String mm = month;
        final String yy = expYear.getText().toString();
        final String stringYear = year + "";
        showProgressLoading();
        MidtransSDK.getInstance().cardRegistration(cardNumber.getText().toString().replace(" ", ""),
                cvv.getText().toString(), mm, stringYear.substring(0,2) + yy, new CardRegistrationCallback() {
                    @Override
                    public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                        aPresenter.addCreditcard(cardRegistrationResponse.getMaskedCard(),
                                cardRegistrationResponse.getSavedTokenId(), mm,
                                stringYear.substring(0, 2) + yy);
                    }

                    @Override
                    public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
                        hideProgresLoading();
                        switch (cardRegistrationResponse.getStatusCode()) {
                            case "400":
                                onFailureRequest("Data kartu kredit tidak valid");
                                break;
                            default:
                                onFailureRequest(s);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        hideProgresLoading();
                        onFailureRequest(ResponeError.getErrorMessage(throwable));
                    }
                });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new InputCreditcardPresenterImpl();
        boolean isHaveCC = getIntent().getBooleanExtra("isHaveList", false);
        aPresenter = new AddCreditcardPresenterImpl(this, this, isHaveCC);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void addTextChangeListener(final EditText editText) {

        editText.addTextChangedListener(new TextWatcherAdapter() {
            private String current = StringUtils.EMPTY;

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.reformatCardNumber(current, editText, this);
            }
        });
    }

    private void onScanPress() {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isGrantedUsingCamera = true;
        } else {

            Toast.makeText(this, "Perizinan ditolak untuk mengakses kamera", Toast.LENGTH_SHORT).show();
        }
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
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    public void onSuccessAddCreditcard() {
        MethodUtil.showCustomToast(this, "Kartu kredit anda berhasil ditambahkan", 0);
        finish();
    }
}
