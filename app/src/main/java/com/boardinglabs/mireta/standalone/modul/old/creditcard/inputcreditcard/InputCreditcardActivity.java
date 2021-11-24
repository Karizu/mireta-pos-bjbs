package com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GCard;
import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.component.util.TextWatcherAdapter;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardPresenter;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardPresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard.SavedCreditcardPresenter;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard.SavedCreditcardPresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard.SavedCreditcardView;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.webview.VeritransWebViewActivity;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.QuickResponse;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;

import org.parceler.Parcels;
import org.parceler.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.BuildConfig.CLIENT_KEY;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity.PAYMENT_ID;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity.QUICK_RESPONSE;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity.TOPUP_RESPONSE;

/**
 * Created by Dhimas on 2/2/18.
 */

public class InputCreditcardActivity extends BaseActivity implements CommonInterface, InputCreditcardView, SavedCreditcardView {
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String NOTE = "note";
    public static final String VOUCHER_ID = "voucherId";
    public static final String MERCHANT_ID = "merchantId";
    public static final String CARD_TYPE = "cardType";
    public static final String IS_TOPUP = "isTopup";


    public static final int MY_SCAN_REQUEST_CODE = 101;
    private InputCreditcardPresenter mPresenter;
    private EditText cardNumber;
    private EditText expMonth;
    private EditText expYear;
    private EditText cvv;
    private ImageView scanCard;
    private CreditcardPresenter cPresenter;
    private SavedCreditcardPresenter sPresenter;
    private Button nextBtn;
    private String tokenTransaction;
    private String merchantId;
    private TransactionTopupResponse topupResponse;
    private String totalAmountAfterCharge;
    private String paymentId;
    private QuickResponse quickResponse;
    private String transactionID;
    private boolean isHaveCreditcard;
    private String cardToken;
    private RelativeLayout containerAmount;
    private TextView amountTxt;
    private TextView ccTitle;
    private CheckBox saveCard;

    private static final String TOPUP_TITLE = "TOP UP Saldo";
    private static final String BAYAR_TITLE = "Bayar";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.add_creditcard_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Kartu Kredit");
        cardNumber = (EditText) findViewById(R.id.no_card);
        expMonth = (EditText) findViewById(R.id.exp_month);
        expYear = (EditText) findViewById(R.id.exp_year);
        cvv = (EditText) findViewById(R.id.cvv);
        scanCard = (ImageView) findViewById(R.id.scan_card);
        nextBtn = (Button) findViewById(R.id.next_btn);
        addTextChangeListener(cardNumber);
        containerAmount = (RelativeLayout) findViewById(R.id.container_topup_credit);
        containerAmount.setVisibility(View.VISIBLE);
        saveCard = (CheckBox) findViewById(R.id.save_card);
        amountTxt = (TextView) findViewById(R.id.amount_topup);
        ccTitle = (TextView) findViewById(R.id.cc_title);
        paymentId = getIntent().getStringExtra(PAYMENT_ID);
        quickResponse = Parcels.unwrap(getIntent().getParcelableExtra(QUICK_RESPONSE));
        topupResponse = new TransactionTopupResponse();

        if (getIntent().getBooleanExtra(IS_TOPUP, false)) {
            ccTitle.setText(InputCreditcardActivity.TOPUP_TITLE);
        } else {
            ccTitle.setText(InputCreditcardActivity.BAYAR_TITLE);
        }

        if (getIntent().getStringExtra(MERCHANT_ID) != "") {
            merchantId = getIntent().getStringExtra(MERCHANT_ID);
        } else {
            merchantId = quickResponse.getMerchant_id();
        }

        try {
            amountTxt.setText(MethodUtil.toCurrencyFormat(quickResponse.getTotalAmount()));
        } catch (Exception e) {
            amountTxt.setText(MethodUtil.toCurrencyFormat(getIntent().getStringExtra(TOTAL_AMOUNT)));
//            MethodUtil.showCustomToast(this, "total transaksi tidak ditemukan", R.drawable.ic_error_login);
        }

        RxView.clicks(scanCard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onScanPress();
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                GAgent agent = PreferenceManager.getAgent();
                if (agent != null) {
                    if (getIntent().getBooleanExtra(IS_TOPUP, false)) {

                        TransactionRequest transactionRequest = new TransactionRequest(quickResponse.getPaymentId(), Double.parseDouble(quickResponse.getTotalAmount()));
                        CustomerDetails customerDetails = new CustomerDetails(agent.name, agent.name, agent.email, agent.mobile);
                        transactionRequest.setCustomerDetails(customerDetails);

                        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
                        ItemDetails itemDetails = new ItemDetails();
                        itemDetails.setQuantity(1);
                        itemDetails.setPrice(Integer.parseInt(quickResponse.getTotalAmount()));
                        itemDetails.setId("1");
                        itemDetails.setName("Payment " + quickResponse.getOrderId());
                        itemDetailsList.add(itemDetails);
                        transactionRequest.setItemDetails(itemDetailsList);

                        com.midtrans.sdk.corekit.models.snap.CreditCard creditCard = new com.midtrans.sdk.corekit.models.snap.CreditCard();
                        creditCard.setSecure(true);
                        creditCard.setSaveCard(true);
                        transactionRequest.setCreditCard(creditCard);
                        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

                        topupResponse.setInfo(getIntent().getStringExtra(NOTE));
                        String[] dateTime = MethodUtil.formatDateAndTime(quickResponse.getCreateAt());
                        topupResponse.setDate(dateTime[0]);
                        topupResponse.setTime(dateTime[1]);
                        topupResponse.setTopupSaldo(quickResponse.getTotalAmount());
                        topupResponse.setBankName("Credit Card");
                        nextMidtrans(merchantId);
                    } else {
                        cPresenter.onFastTransaction("", getIntent().getStringExtra(TOTAL_AMOUNT), getIntent().getStringExtra(NOTE), false, "",
                                getIntent().getStringExtra(VOUCHER_ID), "", agent.name, agent.email, agent.mobile, merchantId, "", "", getIntent().getStringExtra(CARD_TYPE));
                    }
                }
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new InputCreditcardPresenterImpl();
        cPresenter = new CreditcardPresenterImpl(this, this);
        sPresenter = new SavedCreditcardPresenterImpl(this, this);
        sPresenter.getListCard();
//        cPresenter.calculateAmount(getIntent().getStringExtra(TOTAL_AMOUNT), "credit", merchantId);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_SCAN_REQUEST_CODE:
                if (data != null) {
                    if (data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                        CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                        if (!TextUtils.isEmpty(scanResult.cardNumber)) cardNumber.setText(scanResult.cardNumber);
                        if (!TextUtils.isEmpty(scanResult.cvv)) cvv.setText(scanResult.cvv);
                        String expiredResultMonth;
                        if (scanResult.expiryMonth < 10) {
                            expiredResultMonth = "0" + scanResult.expiryMonth;
                        } else {
                            expiredResultMonth = "" + scanResult.expiryMonth;
                        }
                        String year = scanResult.expiryYear + "";
                        expYear.setText(year.substring(2));
                        expMonth.setText(expiredResultMonth);
                    }
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String cardToken = data.getExtras().getString("cardToken");
                    onChargeTransaction(cardToken, tokenTransaction);
                }
                break;
        }

    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().hide();
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
    public void onSuccessTransactionMidtrans(GCreditCard gCreditCard) {
        TransactionRequest transactionRequest = new TransactionRequest(gCreditCard.payment.id, Double.parseDouble(gCreditCard.amount_charged.replace(".", "")));
        CustomerDetails customerDetails = new CustomerDetails(gCreditCard.customer_name, gCreditCard.customer_name, gCreditCard.customer_email, gCreditCard.customer_phone);
        transactionRequest.setCustomerDetails(customerDetails);

        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setQuantity(1);
        itemDetails.setPrice(Integer.parseInt(gCreditCard.amount_charged));
        itemDetails.setId("1");
        itemDetails.setName(gCreditCard.notes + " " + gCreditCard.id);
        itemDetailsList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsList);

        com.midtrans.sdk.corekit.models.snap.CreditCard creditCard = new com.midtrans.sdk.corekit.models.snap.CreditCard();
        creditCard.setSecure(true);
        creditCard.setSaveCard(true);
        transactionRequest.setCreditCard(creditCard);
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        topupResponse.setInfo(gCreditCard.notes);
        String[] dateTime = MethodUtil.formatDateAndTime(gCreditCard.created_at);
        topupResponse.setDate(dateTime[0]);
        topupResponse.setTime(dateTime[1]);
        topupResponse.setTopupSaldo(gCreditCard.amount_charged);
        topupResponse.setBankName("Credit Card");
        nextMidtrans(merchantId);
    }

    @Override
    public void setTotalAmount(String totalAmount) {
        totalAmountAfterCharge = totalAmount;
    }

    @Override
    public void onSuccessTransaction() {
        onCompleteTransaction();
    }

    private void nextMidtrans(String merchantId) {
        showProgressLoading();
        MidtransSDK.getInstance().checkout(merchantId, new CheckoutCallback() {
            @Override
            public void onSuccess(Token token) {
                hideProgresLoading();
                tokenTransaction = token.getTokenId();
                if (isHaveCreditcard) {
                    getCardSavedCardToken();
                } else {
                    getCardToken();
                }

            }

            @Override
            public void onFailure(Token token, String s) {
                hideProgresLoading();
                onFailureRequest(s);
            }

            @Override
            public void onError(Throwable throwable) {
                hideProgresLoading();
                onFailureRequest(ResponeError.getErrorMessage(throwable));
            }
        });
    }

    private void getCardSavedCardToken() {
        CardTokenRequest mCardTokenRequest = new CardTokenRequest();
        mCardTokenRequest.setCardCVV(cvv.getText().toString());
        mCardTokenRequest.setTwoClick(true);
        mCardTokenRequest.setSecure(true);
        mCardTokenRequest.setGrossAmount(Double.parseDouble(topupResponse.getTopupSaldo()));
        mCardTokenRequest.setSavedTokenId(cardToken);
        mCardTokenRequest.setClientKey(CLIENT_KEY);
        showProgressLoading();
        MidtransSDK.getInstance().getCardToken(mCardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {
                hideProgresLoading();
                String urlString = tokenDetailsResponse.getRedirectUrl();
                if (!TextUtils.isEmpty(urlString)) {
                    Intent intent = new Intent(InputCreditcardActivity.this, VeritransWebViewActivity.class);
                    intent.putExtra("redirectUrl", urlString);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onFailure(TokenDetailsResponse tokenDetailsResponse, String s) {
                hideProgresLoading();
                onFailureRequest(s);
            }

            @Override
            public void onError(Throwable throwable) {
                hideProgresLoading();
                onFailureRequest(ResponeError.getErrorMessage(throwable));
            }
        });
    }

    private void getCardToken() {
        CardTokenRequest mCardTokenRequest = new CardTokenRequest(cardNumber.getText().toString().replace(" ",""), cvv.getText().toString(),
                expMonth.getText().toString(), "20"+expYear.getText().toString(),
                CLIENT_KEY);
        mCardTokenRequest.setSecure(true);
        mCardTokenRequest.setGrossAmount(Double.parseDouble(topupResponse.getTopupSaldo()));
        mCardTokenRequest.setBank("bri");
        mCardTokenRequest.setChannel("migs");
        showProgressLoading();
        MidtransSDK.getInstance().getCardToken(mCardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {
                String cardToken = tokenDetailsResponse.getTokenId();
                String urlString = tokenDetailsResponse.getRedirectUrl();
                if (!TextUtils.isEmpty(urlString)) {
                    Intent intent = new Intent(InputCreditcardActivity.this, VeritransWebViewActivity.class);
                    intent.putExtra("redirectUrl", urlString);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onFailure(TokenDetailsResponse tokenDetailsResponse, String s) {
                hideProgresLoading();
                onFailureRequest(s);
            }

            @Override
            public void onError(Throwable throwable) {
                hideProgresLoading();
                onFailureRequest(ResponeError.getErrorMessage(throwable));
            }
        });
    }

    private void onChargeTransaction(String tokenCard, String tokenTransaction) {
        CreditCardPaymentModel model = new CreditCardPaymentModel(tokenCard, false);
        String tokentT = new String(tokenTransaction);
        showProgressLoading();
        MidtransSDK.getInstance().paymentUsingCard(tokentT, model, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
//                transactionResponse.getMaskedCard()
                String savedTokenId = transactionResponse.getSavedTokenId();
                String expiredToken = transactionResponse.getSavedTokenIdExpiredAt();
                transactionID = transactionResponse.getOrderId();
                if (saveCard.isChecked()) {
                    registerCreditcard(savedTokenId, expiredToken);
                } else {
                    onCompleteTransaction();
                }


            }

            @Override
            public void onFailure(TransactionResponse transactionResponse, String s) {
                hideProgresLoading();
                onFailureRequest(s);
            }

            @Override
            public void onError(Throwable throwable) {
                hideProgresLoading();
                onFailureRequest(ResponeError.getErrorMessage(throwable));
            }
        });
    }

    private void registerCreditcard(final String savedTokenId, final String expiryToken) {
        MidtransSDK.getInstance().cardRegistration(cardNumber.getText().toString().replace(" ", ""),
                cvv.getText().toString(), expMonth.getText().toString(), "20" + expYear.getText().toString(),
                new CardRegistrationCallback() {
                    @Override
                    public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                        String[] user = PreferenceManager.getUserInfo();
                        cPresenter.addCreditcard(cardRegistrationResponse.getSavedTokenId(), cardRegistrationResponse.getMaskedCard(),
                                user[0], expMonth.getText().toString(), "20" + expYear.getText().toString(), expiryToken);
                    }

                    @Override
                    public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
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

                    }
                });
    }

    private void onCompleteTransaction() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSuccessTransaction(transactionID);
            }
        }, 6000);
    }

    private void onSuccessTransaction(String orderId) {
        Intent intent = new Intent(InputCreditcardActivity.this, StatusTopupActivity.class);
//        mTransaction.setMerchantName(quickResponse.merchantName);
//        intent.putExtra(Constant.TRANSACTION, Parcels.wrap(mTransaction));
//        intent.putExtra("transactionId", orderId);
        topupResponse.setSuccess(true);
        topupResponse.setOrderId(orderId);
        intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
        startActivity(intent);
    }

    @Override
    public void onSuccessListCreditcard(List<GCard> cardList) {
        if (cardList != null && cardList.size() > 0) {
            for (GCard card : cardList) {
                if (card.is_default.equalsIgnoreCase("1")) {
                    isHaveCreditcard = true;
                    cardToken = card.card_token;
                    scanCard.setEnabled(false);
                    cardNumber.setEnabled(false);
                    expMonth.setEnabled(false);
                    expYear.setEnabled(false);
                    cardNumber.setText("************" + card.cardhash.substring(card.cardhash.length() - 4));
                    expYear.setText(card.expiry_year.substring(card.expiry_year.length() - 2));
                    expMonth.setText(card.expiry_month);
                }
            }
        }
    }

    @Override
    public void onSuccessDeleteCard() {

    }

}
