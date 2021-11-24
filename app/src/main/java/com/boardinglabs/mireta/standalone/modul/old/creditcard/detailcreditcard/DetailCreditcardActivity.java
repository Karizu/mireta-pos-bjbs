package com.boardinglabs.mireta.standalone.modul.old.creditcard.detailcreditcard;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/16/18.
 */

public class DetailCreditcardActivity extends BaseActivity implements CommonInterface, DetailCreditcardPresenterImpl.ActionDetailCreditcardListener {
    private DetailCreditcardPresenter mPresenter;
    private TextView activateBtn;
    private ImageView iconCreditCard;
    private String cardToken;
    private EditText cardNumber;
    private EditText expiredCard;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.detail_creditcard_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Kartu Tersimpan");
        activateBtn = (TextView) findViewById(R.id.activate_button);
        cardNumber = (EditText) findViewById(R.id.card_number);
        expiredCard = (EditText) findViewById(R.id.expired_date);
        iconCreditCard = (ImageView) findViewById(R.id.image_card);
        setIntentData(getIntent());
        RxView.clicks(activateBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.setDefault(cardToken);
            }
        });
    }

    private void setIntentData(Intent intent) {
        if (intent != null) {
            cardNumber.setText("**** **** **** " + intent.getStringExtra("card_number"));
            expiredCard.setText(intent.getStringExtra("card_expired"));
            cardToken = intent.getStringExtra("card_token");
            switch (intent.getStringExtra("card_type")) {
                case "visa":
                    iconCreditCard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icn_visa));
                    break;
                case "mastercard":
                    iconCreditCard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icn_master));
                    break;
                default:
                    iconCreditCard.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icn_jcb));
                    break;
            }
        }
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new DetailCreditcardPresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

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
    public void onSuccessSetDefault() {
        MethodUtil.showCustomToast(this, "Kartu ini berhasil sebagai kartu aktif transaksi pembayaran anda", 0);
    }
}
