package com.boardinglabs.mireta.standalone.modul.old.transactionreview;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.QRTransactionResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.checkpasscode.CheckPasscodeActivity;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardPresenter;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardPresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardView;
import com.boardinglabs.mireta.standalone.modul.old.purchase.PurchaseActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.QuickResponse;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;

import org.parceler.Parcels;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.CARD_TYPE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.MERCHANT_ID;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.NOTE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.TOTAL_AMOUNT;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.VOUCHER_ID;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity.TOPUP_RESPONSE;

/**
 * Created by Randy on 02/14/19.
 */

public class TransactionReviewActivity extends BaseActivity implements CommonInterface, TransactionReviewView, InputCreditcardView {
    public static final String TRANSACTION_INFO = "transactionInfo";
    public static final String TRANSACTION_INFO_MORE = "transactionInfoMore";
    public static final String TOTAL_TRANSACTION = "totalTransaction";
    public static final String ORDER_ID = "orderId";
    public static final String REQUEST_FROM = "requestFrom";
    public static final String IMAGE_URL = "imgUrl";
    public static final String IMAGE_DRAWABLE = "imgDrawable";
    public static final String IS_FROM_QR = "isFromQr";
    public static final String IS_FROM_PURCHASE = "isFromPurchase";

    private static final int CHECK_PASSCODE = 1;
    private static final int CHECK_PASSCODE_QR = 2;
    private TextView transactionInfo;
    private TextView transactionInfoMore;
    private ImageView iconTransaction;
    private TextView orderIdText;
    private TextView totalTransaction;
    private TextView balance;
    private TextView errorBalance;
    private TextView fee;
    private TextView transaction;
    private TextView transactionAmount;
    private Button nextBtn;
    private String total;
    private TransactionReviewPresenter mPresenter;
    private int positionRequest;
    private LinearLayout topupBtn;
    private EditText refferal;
    private LinearLayout ccBtn;
    private ImageView ccImg;
    private int selectedPayment = 0;
    private TextView saldoTxt;
    private ImageView transactionIcon;
    private TextView CCtext;
    private boolean isFromPurchase;
    private QuickResponse quickResponse;
    private CreditcardPresenter cPresenter;
    private TransactionTopupResponse topupResponse;
    private String transactionId;
    private String orderId;
    private boolean isFromQr;
    private ImageView iconCheck;
    private ImageView iconCheckCC;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.transaction_review_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Detail Pembayaran");
        transactionInfo = (TextView) findViewById(R.id.transaction_info);
        transactionInfoMore = (TextView) findViewById(R.id.transaction_info_more);
        iconTransaction = (ImageView) findViewById(R.id.icon_transaction);
        orderIdText = (TextView) findViewById(R.id.order_id_text);
        totalTransaction = (TextView) findViewById(R.id.total_transaction);
        balance = (TextView) findViewById(R.id.balance);
        errorBalance = (TextView) findViewById(R.id.error_balance);
        nextBtn = (Button) findViewById(R.id.next_btn);
        topupBtn = (LinearLayout) findViewById(R.id.topup_btn);
        refferal = (EditText) findViewById(R.id.refferal_code);
        fee = (TextView) findViewById(R.id.fee);
        transaction = (TextView) findViewById(R.id.transaction);
        transactionAmount = (TextView) findViewById(R.id.transaction_amount);
        ccBtn = (LinearLayout) findViewById(R.id.cc_btn);
        ccImg = (ImageView) findViewById(R.id.cc_image);
        transactionIcon = (ImageView) findViewById(R.id.transaction_icon);
        saldoTxt = (TextView) findViewById(R.id.saldo_text);
        CCtext = (TextView) findViewById(R.id.CC_text);
        topupResponse = new TransactionTopupResponse();
        iconCheck = (ImageView) findViewById(R.id.iconCheck);
        iconCheckCC = (ImageView) findViewById(R.id.iconCheckCC);
//        if (!PreferenceManager.getStatusAkupay()) {
            iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_merchant));
//        }
        initEvent();
    }

    private void initEvent() {
        String info = getIntent().getStringExtra(TRANSACTION_INFO);
        String moreInfo = getIntent().getStringExtra(TRANSACTION_INFO_MORE);
        Log.i("kunam", getIntent().getStringExtra(TOTAL_TRANSACTION));
        total = getIntent().getStringExtra(TOTAL_TRANSACTION);
        String id = getIntent().getStringExtra(ORDER_ID);
        positionRequest = getIntent().getIntExtra(REQUEST_FROM, 0);
        transactionInfo.setText(info);
        transactionInfoMore.setText(moreInfo);
        orderIdText.setText(id);
        totalTransaction.setText("Rp " +MethodUtil.toCurrencyFormat(total));
        refferal.setText(PreferenceManager.getRefferalId());
        isFromQr = getIntent().getBooleanExtra(IS_FROM_QR, false);
        transaction.setText(info);
        transactionAmount.setText(MethodUtil.toCurrencyFormat(total));
        String imgUrl = getIntent().getStringExtra(IMAGE_URL);
        int imgDraw = getIntent().getIntExtra(IMAGE_DRAWABLE, 0);
        isFromPurchase = getIntent().getBooleanExtra(IS_FROM_PURCHASE, false);
        if (isFromPurchase) {
            ccBtn.setVisibility(View.GONE);
        } else {
            quickResponse = PreferenceManager.getQrResponse();
        }

        RxView.clicks(ccBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ccBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.border_round_blue_contact));
                topupBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.border_round_white));
                selectedPayment = 2;
                mPresenter.calculate(transactionAmount.getText().toString().replace(".",""), "credit", quickResponse.getMerchant_id());
                iconCheck.setVisibility(View.GONE);
                iconCheckCC.setVisibility(View.VISIBLE);
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switch (positionRequest) {
                    case 100:
                        if (TextUtils.isEmpty(refferal.getText().toString())) {
                            mPresenter.onRegisterPremium("");
                        } else {
                            mPresenter.checkRefferal(refferal.getText().toString());
                        }

                        break;
                    default:
                        switch (selectedPayment) {
                            case 1:
                                if (errorBalance.getVisibility() == View.VISIBLE) {
                                    MethodUtil.showCustomToast(TransactionReviewActivity.this, "Saldo anda tidak mencukupi", R.drawable.ic_error_login);
                                } else {
                                    if (isFromQr) {
                                        Intent intent = new Intent(TransactionReviewActivity.this, CheckPasscodeActivity.class);
                                        String[] user = PreferenceManager.getUserInfo();
                                        intent.putExtra(OtpActivity.MOBILE, user[1]);
                                        intent.putExtra(CheckPasscodeActivity.TRANSACTION_KEY, CheckPasscodeActivity.TRANSACTION_PPOB);
                                        intent.putExtra("hinttext", "Ketik kode PIN untuk validasi transaksi.");
                                        startActivityForResult(intent, CHECK_PASSCODE_QR);
                                    } else {
                                        Intent intent = new Intent(TransactionReviewActivity.this, CheckPasscodeActivity.class);
                                        String[] user = PreferenceManager.getUserInfo();
                                        if (user != null) {
                                            intent.putExtra(OtpActivity.MOBILE, user[1]);
                                            intent.putExtra("hinttext", "Ketik kode PIN untuk validasi transaksi.");
                                        }
                                        startActivityForResult(intent, CHECK_PASSCODE);
                                    }
                                }
                                break;
                            case 2:
                                Intent intentCC = new Intent(TransactionReviewActivity.this, InputCreditcardActivity.class);
                                if (isFromQr) {
                                    intentCC.putExtra(TOTAL_AMOUNT, transactionAmount.getText().toString().replace(".",""));
                                    intentCC.putExtra(VOUCHER_ID, quickResponse.getVoucherId());
                                    intentCC.putExtra(MERCHANT_ID, quickResponse.getMerchant_id());
                                    intentCC.putExtra(CARD_TYPE, "credit");
                                    intentCC.putExtra(NOTE, quickResponse.getNotes());
                                    intentCC.putExtra(InputCreditcardActivity.IS_TOPUP, false);
                                } else {

                                }

                                startActivity(intentCC);
                                break;
                            default:
                                MethodUtil.showCustomToast(TransactionReviewActivity.this, "Harap pilih metode pembayaran", R.drawable.ic_error_login);
                                break;
                        }
                        break;
                }

            }
        });

        if (positionRequest != 100) {
            refferal.setVisibility(View.GONE);
        }

        RxView.clicks(topupBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ccBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.border_round_white));
                topupBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.border_round_blue_contact));
                selectedPayment = 1;
                totalTransaction.setText("Rp " +transactionAmount.getText().toString());
                fee.setText("0");
                iconCheck.setVisibility(View.VISIBLE);
                iconCheckCC.setVisibility(View.GONE);

                float scale = getResources().getDisplayMetrics().density;
                int verticalPad = (int) (5*scale + 0.5f);
//                topupBtn.setPadding(verticalPad*2,verticalPad,verticalPad*2,verticalPad);
            }
        });

        switch (positionRequest) {
            case Constant.PDAM:
                iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_pdam));
                break;
            case Constant.LISTRIK:
                iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_token));
                break;
            case Constant.PULSA_HANDPHONE:
                if (imgDraw != 0) {
                    iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, imgDraw));
                }
                break;
            case Constant.INTERNET_DATA:
                if (imgDraw != 0) {
                    iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, imgDraw));
                }
                break;
            case Constant.INTERNET:
                iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_internet));
                break;
            case Constant.TV_INTERNET:
                iconTransaction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_tv_kabel));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new TransactionReviewPresenterImpl(this, this);
        cPresenter = new CreditcardPresenterImpl(this, this);
        mPresenter.getBalance();
        if (isFromQr) {
            GAgent agent = PreferenceManager.getAgent();
            if (quickResponse != null && agent != null) {
                cPresenter.onFastTransaction("", total, quickResponse.getNotes(), false, "",
                        quickResponse.getVoucherId()!=null?quickResponse.getVoucherId():"", "", agent.name, agent.email, agent.mobile, quickResponse.getMerchant_id(), "", "", "wallet");
            } else {
                MethodUtil.showCustomToast(TransactionReviewActivity.this, Constant.EXPIRED_SESSION, R.drawable.ic_error_login);
                goToLoginPage1(this);
            }

        }

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
        progressBar.show(this, "Pembayaran anda sedang dalam proses", false, null);
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
//        else{
//            onBackPressed();
//        }
    }

    @Override
    public void onSuccessGetBalance(String balance) {
        this.balance.setText("Rp " + MethodUtil.toCurrencyFormat(balance));
        int curBalance = Integer.parseInt(balance);
        int totalAmount = TextUtils.isEmpty(total) ? 0 : Integer.parseInt(total);
        if (curBalance < totalAmount) {
            errorBalance.setVisibility(View.VISIBLE);
        } else {
            errorBalance.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSuccessRegisterPremium(MessageResponse mResponse) {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onSuccessPayTransaction(GTransaction transaction) {
//        Log.i("kunam", transaction.customer_no);
//        Log.i("kunam", transaction.customer.id);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PurchaseActivity.ORDER_ID, transaction.id);
        resultIntent.putExtra(PurchaseActivity.TOTAL_AMOUNT, transaction.default_price);
        resultIntent.putExtra(PurchaseActivity.NOTE, transaction.service.name + ", " + transaction.customer_no);
        resultIntent.putExtra(PurchaseActivity.DATA, transaction.data);
        resultIntent.putExtra(PurchaseActivity.DATE_TRANSACTION, transaction.created_at);
        resultIntent.putExtra(PurchaseActivity.SERVICE_PROVIDER, positionRequest);
//        resultIntent.putExtra("", positionRequest);
//        resultIntent.putExtra(PurchaseActivity.ORDER_ID, "219");
//        resultIntent.putExtra(PurchaseActivity.TOTAL_AMOUNT, "20400");
//        resultIntent.putExtra(PurchaseActivity.NOTE, "PAM PALYJA" + ", " + "132233445566");
//        resultIntent.putExtra(PurchaseActivity.DATA, "{\"provider_code\":\"11\",\"customer_id\":\"132233445566\",\"customer_name\":\"CUSTOMER MITRACOM 3 BILL\",\"bill_total\":\"03\",\"bill_date\":\"201108\",\"bill_amount\":\"20000\",\"penalty\":\"00000000\",\"cubication\":\"00000000-00000000\",\"ref_no\":\"54819371\"}");
//        resultIntent.putExtra(PurchaseActivity.DATE_TRANSACTION, "2017-12-26 11:26:03");
//        resultIntent.putExtra(PurchaseActivity.SERVICE_PROVIDER, positionRequest);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onSuccessCheckReferral(String refferalId) {
        mPresenter.onRegisterPremium(refferalId);
    }

    @Override
    public void chargeAmount(String totalAmount, String fee) {
        this.fee.setText(MethodUtil.toCurrencyFormat(fee));
        totalTransaction.setText(MethodUtil.toCurrencyFormat(totalAmount));
    }

    @Override
    public void charge(QRTransactionResponse response) {
//        Log.i("kunam", String.valueOf(response.success));
        if (response.success) {
            topupResponse.setInfo(response.transaction.notes);
            String[] dateTime = MethodUtil.formatDateAndTime(response.transaction.created_at);
            topupResponse.setDate(dateTime[0]);
            topupResponse.setTime(dateTime[1]);
            topupResponse.setTopupSaldo(response.transaction.amount_charged);
            topupResponse.setBankName("Saldo");
            topupResponse.setSuccess(true);
            topupResponse.setJenisTransaksi(2);
            topupResponse.setOrderId(response.transaction.id);
            topupResponse.setMerchant_name(response.transaction.merchant_name);
            topupResponse.isFromHome = true;
            Intent intent = new Intent(TransactionReviewActivity.this, StatusTopupActivity.class);
            intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
            startActivity(intent);
        }
        else {
            onFailureRequest("Terjadi kesalahan koneksi, tunggu beberapa saat lagi");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHECK_PASSCODE:
                    mPresenter.payTransaction(orderIdText.getText().toString());
                    break;
                case CHECK_PASSCODE_QR:
                    mPresenter.chargeTransaction(transactionId);
                    break;
            }
        }
    }

    @Override
    public void onSuccessTransactionMidtrans(GCreditCard gCreditCard) {
        orderId = gCreditCard.id;
        orderIdText.setText(orderId);
        transactionId = gCreditCard.id;
//        mPresenter.chargeTransaction(gCreditCard.id);
    }

    @Override
    public void setTotalAmount(String totalAmount) {

    }

    @Override
    public void onSuccessTransaction() {

    }
}
