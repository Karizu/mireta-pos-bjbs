package com.boardinglabs.mireta.standalone.modul.old.topup.topup;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GTopup;
import com.boardinglabs.mireta.standalone.component.network.gson.GVoucher;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.QuickResponse;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.verify.VerifyActivity;

import org.parceler.Parcels;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity.QUICK_RESPONSE;

/**
 * Created by Dhimas on 9/27/17.
 */

public class TopupActivity extends BaseActivity implements TopupView, CommonInterface{
    private static final String VOUCHER_NOT_VALID = "Voucher is not valid.";
    private RelativeLayout dropdownTopup;
    private RelativeLayout containerTopup;
    private int position;
    private TextView topupValueText;
    private EditText inputTopupValue;
    private Button nextBtn;
    private String voucherId;
    private ImageView statusLogo;
    private EditText inputVoucher;
    private TopupPresenter mPresenter;
    private ImageView verifyId;
    private Context mContext;

    private Button bankTransferPopUpButton;

    private LinearLayout bankTransferButton;
    private LinearLayout kartuKreditButton;
    private LinearLayout virtualAccountButton;

    private Button button5;
    private Button button10;
    private Button button25;
    private Button button50;
    private Button button100;
    private Button button500;
    private Button button1000;
    private Button buttonKetik;
    private TextView rpText;

    private int selectedMethodPayment = 99;


    private RelativeLayout containerMetodaPembayaran;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.topup_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("TOPUP");
        mContext = this;
        position = 8;
        dropdownTopup = (RelativeLayout) findViewById(R.id.container_topup_dialog);
        containerTopup = (RelativeLayout) findViewById(R.id.container_topup_value);
        topupValueText = (TextView) findViewById(R.id.value_topup_txt);
        inputTopupValue = (EditText) findViewById(R.id.input_value_topup);
        nextBtn = (Button) findViewById(R.id.next_btn);
        statusLogo = (ImageView) findViewById(R.id.status_voucher_logo);
        inputVoucher = (EditText) findViewById(R.id.voucher_field);
        verifyId = (ImageView) findViewById(R.id.verify_id);

        bankTransferPopUpButton = (Button) findViewById(R.id.bankTransferPopUpButton);
        bankTransferButton = (LinearLayout) findViewById(R.id.bankTransferButton);
        kartuKreditButton = (LinearLayout) findViewById(R.id.kartuKreditButton);
        virtualAccountButton = (LinearLayout) findViewById(R.id.virtualAccountButton);
        containerMetodaPembayaran = (RelativeLayout) findViewById(R.id.container_metoda_pembayaran);

        button5 = (Button) findViewById(R.id.button5);
        button10 = (Button) findViewById(R.id.button10);
        button25 = (Button) findViewById(R.id.button25);
        button50 = (Button) findViewById(R.id.button50);
        button100 = (Button) findViewById(R.id.button100);
        button500 = (Button) findViewById(R.id.button500);
        button1000 = (Button) findViewById(R.id.button1000);
        buttonKetik = (Button) findViewById(R.id.buttonKetik);

        rpText = (TextView) findViewById(R.id.rp_text);

        inputVoucher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(inputVoucher.getText().toString()) &&
                            inputVoucher.getText().toString().length() > 4) {
                        mPresenter.checkVoucher(inputVoucher.getText().toString(), getAmount());
                    }
                }
                return false;
            }
        });

        inputVoucher.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!TextUtils.isEmpty(inputVoucher.getText().toString()) &&
                            inputVoucher.getText().toString().length() > 4) {
                        mPresenter.checkVoucher(inputVoucher.getText().toString(), getAmount());
                    }
                }
            }
        });

        RxView.clicks(verifyId).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(TopupActivity.this, VerifyActivity.class));
            }
        });

        RxView.clicks(dropdownTopup).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showTopup();
            }
        });

        RxView.clicks(containerTopup).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button5).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 0;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("5.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button10).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 1;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("10.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button25).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 2;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("25.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button50).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 3;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("50.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button100).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 4;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("100.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button500).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 5;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("500.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(button1000).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 6;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setTextColor(getResources().getColor(R.color.black));
                topupValueText.setText("1.000.000");
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(buttonKetik).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                position = 7;
                rpText.setVisibility(View.VISIBLE);
                topupValueText.setVisibility(View.GONE);
                inputTopupValue.setVisibility(View.VISIBLE);
                inputTopupValue.requestFocus();
                inputTopupValue.setHint("Masukan Nominal");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputTopupValue, InputMethodManager.SHOW_IMPLICIT);
                containerTopup.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
            }
        });

        RxView.clicks(inputTopupValue).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String valueTopup = inputTopupValue.getText().toString();
                topupValueText.setText(MethodUtil.toCurrencyFormat(valueTopup));
                topupValueText.setVisibility(View.VISIBLE);
                inputTopupValue.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputTopupValue.getWindowToken(), 0);
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (selectedMethodPayment!=99) {
                    if(Double.parseDouble(getAmount())>0) {
                        String[] user = PreferenceManager.getUserInfo();
                        if(user[2].equalsIgnoreCase("0")){
//                            Log.i("kunam", String.valueOf(selectedMethodPayment));
                            if(selectedMethodPayment==1){
                                Toast.makeText(mContext, "Maaf. Untuk melakukan top up dengan Kartu Kredit silakan hubungi Customer Service kami melalui WhatsApp ke nomor +62 811-8884-332", Toast.LENGTH_LONG).show();
                            }else{
                                mPresenter.topup(getAmount(), voucherId, String.valueOf(selectedMethodPayment));
                            }
                        }else{
                            mPresenter.topup(getAmount(), voucherId, String.valueOf(selectedMethodPayment));
                        }
                    }else{
                        Toast.makeText(mContext, "Nominal topup tidak boleh kosong", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(mContext, "Anda harus memilih metoda pembayaran", Toast.LENGTH_LONG).show();
                }

            }
        });

        bankTransferPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("kunam", String.valueOf(position));
                if(position<=7) {
                    containerMetodaPembayaran.setVisibility(View.VISIBLE);
                    setToolbarTitle("METODE PEMBAYARAN");
                }else{
                    MethodUtil.showCustomToast((TopupActivity)mContext, "Anda harus menginputkan nominal terlebih dahulu", 0);
                }
            }
        });

        virtualAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMethodPayment = 4;
                containerMetodaPembayaran.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
                bankTransferPopUpButton.setText("VIRTUAL ACCOUNT");
                bankTransferPopUpButton.setTextColor(getResources().getColor(R.color.black));
            }
        });

        kartuKreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMethodPayment = 1;
                containerMetodaPembayaran.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
                bankTransferPopUpButton.setText("KARTU KREDIT");
                bankTransferPopUpButton.setTextColor(getResources().getColor(R.color.black));
            }
        });

        bankTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMethodPayment = 0;
                containerMetodaPembayaran.setVisibility(View.GONE);
                setToolbarTitle("TOPUP");
                bankTransferPopUpButton.setText("BANK TRANSFER");
                bankTransferPopUpButton.setTextColor(getResources().getColor(R.color.black));
            }
        });

    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new TopupPresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        if(containerMetodaPembayaran.getVisibility()==View.GONE && containerTopup.getVisibility()==View.GONE) {
            onBackPressed();
        }else{
            containerMetodaPembayaran.setVisibility(View.GONE);
            containerTopup.setVisibility(View.GONE);
            setToolbarTitle("TOPUP");
        }

    }

    @Override
    public void onBackPressed() {
        if(containerMetodaPembayaran.getVisibility()==View.GONE && containerTopup.getVisibility()==View.GONE) {
            super.onBackPressed();
        }else{
            containerMetodaPembayaran.setVisibility(View.GONE);
            containerTopup.setVisibility(View.GONE);
            setToolbarTitle("TOPUP");
        }
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void showTopup() {
        containerTopup.setVisibility(View.VISIBLE);
        setToolbarTitle("NOMINAL");
    }

    private String getAmount() {
        String amount;
        if (position != 7) {
            amount = topupValueText.getText().toString().replace(".", "");
        } else {
            amount = inputTopupValue.getText().toString().replace(".", "");
        }
        return amount;
    }

    @Override
    public void onSuccessRequest(GTopup topup) {
        Intent intent = new Intent(TopupActivity.this, PaymentMethodTopupActivity.class);
        intent.putExtra(PaymentMethodTopupActivity.AMOUNT, topup.amount);
        intent.putExtra(PaymentMethodTopupActivity.UNIQUE, topup.unique);
        intent.putExtra(PaymentMethodTopupActivity.ORDER_ID, topup.order_id);
        intent.putExtra(PaymentMethodTopupActivity.VOUCHER, topup.voucher);
        intent.putExtra(PaymentMethodTopupActivity.EXPIRED, topup.expired_at);
        intent.putExtra(InputCreditcardActivity.IS_TOPUP, true);
        intent.putExtra(PaymentMethodTopupActivity.IS_USING_VA, (selectedMethodPayment==4));
        intent.putExtra(PaymentMethodTopupActivity.IS_USING_CC, (selectedMethodPayment==1));
        if (selectedMethodPayment==1) {
            QuickResponse quickResponse = new QuickResponse();
            quickResponse.setAmount(topup.amount);
            quickResponse.setTotalAmount(topup.payment.amount);
            int fee = Integer.parseInt(topup.payment.amount) - Integer.parseInt(topup.amount);
            quickResponse.setFee(fee+"");
            quickResponse.setMerchant_id(topup.customer.id);
            quickResponse.setPaymentId(topup.payment.id);
            quickResponse.setNotes(topup.payment.object_type);
            quickResponse.setCreateAt(topup.payment.created_at);
            intent.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
        }


        startActivity(intent);

    }


    @Override
    public void onSuccessRequestVoucher(GVoucher voucher) {
        voucherId = voucher.id;
        statusLogo.setImageDrawable(getResources().getDrawable(R.drawable.blue_checklist));
        statusLogo.setVisibility(View.VISIBLE);
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
        switch (msg) {
            case VOUCHER_NOT_VALID:
                statusLogo.setImageDrawable(getResources().getDrawable(R.drawable.voucher_eject));
                MethodUtil.showCustomToast(this, msg, 0);
                statusLogo.setVisibility(View.VISIBLE);
                voucherId = "";
                break;
            case "v":
                break;
            default:
                MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
                if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
                    goToLoginPage1(this);
                }
        }
    }
}
