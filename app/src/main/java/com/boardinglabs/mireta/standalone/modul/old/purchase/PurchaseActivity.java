package com.boardinglabs.mireta.standalone.modul.old.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.MiretaPOSApplication;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyPurchaseQuotaAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GLogo;
import com.boardinglabs.mireta.standalone.component.network.gson.GServices;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.transactionreview.TransactionReviewActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 10/2/17.
 */

public class PurchaseActivity extends BaseActivity implements CommonInterface, PurchaseView, RecyPurchaseQuotaAdapter.Action {
    public static final String DATE_TRANSACTION = "dateTransaction";
    public static final String ORDER_ID = "orderId";
    public static final String NOTE = "note";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String DATA = "data";
    public static final String SERVICE_PROVIDER = "serviceProvider";

    private static String telkomselRegex = "^(\\+62|\\+0|0|62)8(1[123]|52|53|21|22|23)[0-9]{5,9}$";
    private static String simpatiRegex = "^(\\+62|\\+0|0|62)8(1[123]|2[12])[0-9]{5,9}$";
    private static String asRegex = "^(\\+62|\\+0|0|62)8(52|53|23)[0-9]{5,9}$";
    private static String triRegex = "^(\\+62|\\+0|0|62)8(96|97|98|99|95)[0-9]{5,9}$";
    private static String smartfrenRegex = "^(\\+62|\\+0|0|62)8(81|82|83|84|85|86|87|88|89)[0-9]{5,9}$";
    private static String axisRegex = "^(\\+62|\\+0|0|62)8(38|31|32|33)[0-9]{5,9}$";
    private static String indosatRegex = "^(\\+62815|0815|62815|\\+0815|\\+62816|0816|62816|\\+0816|\\+62858|0858|62858|\\+0814|\\+62814|0814|62814|\\+0814)[0-9]{5,9}$";
    private static String im3Regex = "^(\\+62855|0855|62855|\\+0855|\\+62856|0856|62856|\\+0856|\\+62857|0857|62857|\\+0857)[0-9]{5,9}$";
    private static String xlRegex = "^(\\+62817|0817|62817|\\+0817|\\+62818|0818|62818|\\+0818|\\+62819|0819|62819|\\+0819|\\+62859|0859|62859|\\+0859|\\+0878|\\+62878|0878|62878|\\+0877|\\+62877|0877|62877)[0-9]{5,9}$";

    private RecyclerView listPurchase;
    private RecyPurchaseQuotaAdapter quotaAdapter;
    private PurchasePresenter mPresenter;
    private int pos;
    private LinearLayout containerList;
    private Button nextBtn;
    private TextView infoPurchase;
    private TextView pricePurchase;
    private EditText no;
    private RelativeLayout pickService;
    private RelativeLayout inputAmount;
    private EditText amountPurchase;
    private ImageView iconPurchase;
    private List<GServices> listServices;
    private TextView jenisNomor;
    private boolean isTokenPln;
    private int positionService;
    private boolean isPicked;
    private boolean isUsingInquiry = true;
    private int drawableOperator = 0;

    public static final String TYPE_PLN = "typePln";
    public static final int TOKEN_PLN = 0;
    public static final int BAYAR_PLN = 1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.purchase_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        no = (EditText) findViewById(R.id.no);
        pricePurchase = (TextView) findViewById(R.id.price_purchase);
        infoPurchase = (TextView) findViewById(R.id.info_purchase);
        nextBtn = (Button) findViewById(R.id.next_btn);
        containerList = (LinearLayout) findViewById(R.id.container_list_service);
        pickService = (RelativeLayout) findViewById(R.id.pick_service);
        iconPurchase = (ImageView) findViewById(R.id.icon_purchase);
        inputAmount = (RelativeLayout) findViewById(R.id.input_amount);
        amountPurchase = (EditText) findViewById(R.id.amount_purchase);
        listPurchase = (RecyclerView) findViewById(R.id.purchase_list);
        jenisNomor = (TextView) findViewById(R.id.jenis_nomor);
        listPurchase.setLayoutManager(new LinearLayoutManager(this));
        initEvent();
        setHeaderToolbar();
    }

    private void setHeaderToolbar(){
        switch (pos) {
            case Constant.INTERNET_DATA:
                setToolbarTitle("DATA");
                break;
            case Constant.ASURANSI:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_ASURANSI);
                break;
            case Constant.BAZNAS:
                break;
            case Constant.BPJS_KESEHATAN:
                setToolbarTitle("BPJS");
                break;
            case Constant.BPJS_KETENAGAKERJAAN:
                setToolbarTitle("BPJS KETENAGAKERJAAN");
                break;
            case Constant.LISTRIK:
                if (isTokenPln) {
                    mPresenter.getServices(Constant.SERVICE_PURCHASE, "", no.getText().toString(), Constant.SERVICE_PLN);
                } else {
                    mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PLN);
                }
                break;
            case Constant.PULSA_HANDPHONE:
                setToolbarTitle("PULSA");
                break;
            case Constant.PASCABAYAR:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PULSA);
                break;
            case Constant.PDAM:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PDAM);
                break;
            case Constant.TV_INTERNET:
                setToolbarTitle("TV KABEL");break;
            case Constant.CICILAN:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_MULTIFINANCE);
                break;
            case Constant.INTERNET:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_ISP);
                break;
            default:
                break;
        }
    }

    private void initEvent() {
        RxView.clicks(pickService).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(no.getText().toString().length()>0) {
                    processRequest();
                }else{
                    MethodUtil.showCustomToast(PurchaseActivity.this, "Input tidak boleh kosong", R.drawable.ic_error_login);
                }
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(pos == Constant.LISTRIK && !amountPurchase.isShown()) {
                    if (getIntent().getIntExtra(TYPE_PLN, 0) != TOKEN_PLN) {
                        processRequest();
                    }
                }
                if(pos == Constant.BPJS_KESEHATAN){
                    if (no.getText().toString().length() > 1) {
                        mPresenter.setInquiry("61", no.getText().toString(), isUsingInquiry, "0");
                    }else{
                        MethodUtil.showCustomToast(PurchaseActivity.this, "Input tidak boleh kosong", R.drawable.ic_error_login);
                    }
                }else {
                    if (no.getText().toString().length() > 1) {
                        if ((amountPurchase.isShown())) {
                            if (!(TextUtils.isEmpty(amountPurchase.getText().toString())) && listServices != null && listServices.size() > 0 && isPicked) {
                                mPresenter.setInquiry(listServices.get(positionService).id, no.getText().toString(), isUsingInquiry, amountPurchase.getText().toString());
                            } else {
                                MethodUtil.showCustomToast(PurchaseActivity.this, "Mohon lengkapi seluruh data", R.drawable.ic_error_login);
                            }
                        } else {
                            if ((listServices != null && listServices.size() > 0 && isPicked)) {
                                mPresenter.setInquiry(listServices.get(positionService).id, no.getText().toString(), isUsingInquiry, listServices.get(positionService).default_price);
                            }
                        }
                    } else {
                        MethodUtil.showCustomToast(PurchaseActivity.this, "Input tidak boleh kosong", R.drawable.ic_error_login);
                    }
                }
            }
        });

        no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (pos == Constant.PULSA_HANDPHONE || pos == Constant.INTERNET_DATA) {
                        String nomor = charSequence.toString();
                        nomor = charSequence.charAt(0)!='0'?"0"+nomor:nomor;
                        if (nomor.matches(xlRegex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.icon_xl));
                            drawableOperator = R.drawable.icon_xl;
                        } else if (nomor.matches(indosatRegex) || nomor.matches(im3Regex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.icon_indosat));
                            drawableOperator = R.drawable.icon_indosat;
                        } else if (nomor.matches(telkomselRegex) || nomor.matches(simpatiRegex) || nomor.matches(asRegex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.icon_telkomsel));
                            drawableOperator = R.drawable.icon_telkomsel;
                        } else if (nomor.matches(triRegex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.icon_tri));
                            drawableOperator = R.drawable.icon_tri;
                        } else if (nomor.matches(smartfrenRegex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.smartfren));
                            drawableOperator = R.drawable.smartfren;
                        } else if (nomor.matches(axisRegex)) {
                            iconPurchase.setImageDrawable(ContextCompat.getDrawable(PurchaseActivity.this, R.drawable.axis));
                            drawableOperator = R.drawable.axis;
                        } else {
                            iconPurchase.setImageDrawable(null);
                        }
                    }
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>16){
                    MethodUtil.showCustomToast(PurchaseActivity.this, "Maximal input 16 karakter", R.drawable.ic_error_login);
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        });

    }

    private void gotoTransactionReview(String transactionInfoMore, String transactionInfo, String transactionId, int service, String totalTransaction, String urlImage, int drawableLogo) {
        Intent intent = new Intent(this, TransactionReviewActivity.class);
        intent.putExtra(TransactionReviewActivity.TOTAL_TRANSACTION, totalTransaction);
        intent.putExtra(TransactionReviewActivity.ORDER_ID, transactionId);
        intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO, transactionInfo);
        intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO_MORE, transactionInfoMore);
        intent.putExtra(TransactionReviewActivity.REQUEST_FROM, service);
        intent.putExtra(TransactionReviewActivity.IMAGE_URL, urlImage);
        intent.putExtra(TransactionReviewActivity.IMAGE_DRAWABLE, drawableLogo);
        intent.putExtra(TransactionReviewActivity.IS_FROM_PURCHASE, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            TransactionTopupResponse respone = new TransactionTopupResponse();
            String orderId = data.getExtras().getString(ORDER_ID);
            String date = data.getExtras().getString(DATE_TRANSACTION);
            String amount = data.getExtras().getString(TOTAL_AMOUNT);
            String note = data.getExtras().getString(NOTE);
            String dataText = data.getExtras().getString(DATA);
            int service = data.getExtras().getInt(SERVICE_PROVIDER);
            pos = data.getExtras().getInt(Constant.POSITION);
            switch (service) {
                case Constant.LISTRIK:
                    if (!TextUtils.isEmpty(dataText)) {
                        try {
                            JSONObject json = new JSONObject(dataText);
                            if (json.has("harga")) {
                                note = "Token PLN " + Integer.parseInt(json.get("harga").toString()) + " ke " +
                                        json.get("idmeter") + " SUKSES. SN: " +
                                        json.get("token").toString() + "/" +
                                        json.get("nama").toString() +
                                        "/" + json.get("kwh").toString().replaceFirst("^0+(?!$)", "");
                            } else if (json.has("customer_id")) {
                                note = "Pembayaran PLN dengan no " + json.get("customer_id").toString() + " atas nama " + json.get("customer_name").toString()
                                        + " untuk bulan " + json.get("bill_period").toString() + ", kWh : " + json.get("fare_power").toString();
                            } else if (json.has("idpel")) {
                                String bulan = json.get("blth1").toString();
                                bulan = bulan.substring(0,4) + "-" + bulan.substring(4, bulan.length());
                                note = "Pembayaran PLN dengan no " + json.get("idpel").toString() + " atas nama " + json.get("nama").toString()
                                        + " untuk bulan " + bulan + ", kWh : " + json.get("daya").toString().replaceFirst("^0+(?!$)", "");
                            } else {
                                note = "Token PLN " + json.get("price").toString() + " ke " +
                                        json.get("meter_serial") + " SUKSES. SN: " +
                                        json.get("token").toString() + "/" +
                                        json.get("customer_name").toString() +
                                        "/" + json.get("total_kwh").toString();
                            }
                        } catch (JSONException e) {
                            note = "Pembayaran listrik sukses";
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constant.BPJS_KESEHATAN:
                    try {
                        JSONObject json = new JSONObject(dataText);
                        if (json.has("customer_id")) {
                            note = "Pembayaran BPJS dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        } else if (json.has("namapelanggan")) {
                            note = "Pembayaran BPJS dengan no " + json.get("nova").toString() +
                                    " atas nama " + json.get("namapelanggan").toString() + " Sukses.";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.PDAM:
                    try {
                        JSONObject json = new JSONObject(dataText);
                        if (json.has("provider_code")) {
                            note = "Pembayaran PAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + "\n" +
                                    "Periode : " + json.get("bill_date").toString() + "\n" +
                                    "Tagihan :" + json.get("bill_amount").toString().replaceFirst("^0+(?!$)", "") + "\n" + " Sukses.";
                        } else if (json.has("idpel")) {
                            note = "Pembayaran PDAM dengan no " + json.get("idpel").toString() +
                                    " atas nama " + json.get("namapelanggan").toString() + "\nAlamat : " + json.get("alamat").toString() + "\n" +
                                    "Tagihan : " + json.get("totaltagihan").toString().replaceFirst("^0+(?!$)", "") + " Sukses.";
                        } else if (json.has("data")) {
                            JSONObject dataPDAM = new JSONObject(json.getJSONObject("data").toString());
                            note = "Pembayaran PDAM dengan no " + dataPDAM.get("customer_id").toString() +
                                    " atas nama " + dataPDAM.get("customer_name").toString() + "\n" + " Sukses.";
                        } else {
                            note = "Pembayaran PDAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + "\n" +
                                    "Periode : " + json.get("bill_period").toString() + "\n" +
                                    "Pemakaian m3 : " + json.get("used_meters").toString() + "\n" +
                                    "Tagihan :" + json.get("bill_amount").toString().replaceFirst("^0+(?!$)", "") + "\n" + " Sukses.";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.INTERNET:

                default:
                    break;
            }

            String[] dateTime = MethodUtil.formatDateAndTime(date);
            respone.setDate(dateTime[0]);
            respone.setTime(dateTime[1]);
            respone.setSuccess(true);
            respone.setOrderId(orderId);
            respone.setInfo(note);
            respone.setTopupSaldo(amount);
            Intent intent = new Intent(this, StatusTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(respone));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PurchasePresenterImpl(this, this);
        setListProduct(getIntent().getIntExtra(Constant.POSITION, 0));
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(containerList.isShown()){
            containerList.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void processRequest() {
        switch (pos) {
            case Constant.INTERNET_DATA:
                mPresenter.getServices(Constant.SERVICE_PURCHASE, "", no.getText().toString(), Constant.SERVICE_PAKET_DATA);
                break;
            case Constant.ASURANSI:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_ASURANSI);
                break;
            case Constant.BAZNAS:
                break;
            case Constant.BPJS_KESEHATAN:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_BPJS);
                break;
            case Constant.BPJS_KETENAGAKERJAAN:
                break;
            case Constant.LISTRIK:
                if (isTokenPln) {
                    mPresenter.getServices(Constant.SERVICE_PURCHASE, "", no.getText().toString(), Constant.SERVICE_PLN);
                } else {
                    mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PLN);
                }
                break;
            case Constant.PULSA_HANDPHONE:
                mPresenter.getServices(Constant.SERVICE_PURCHASE, "", no.getText().toString(), Constant.SERVICE_PULSA);
                break;
            case Constant.PASCABAYAR:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PULSA);
                break;
            case Constant.PDAM:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_PDAM);
                break;
            case Constant.TV_INTERNET:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_TV);
                break;
            case Constant.CICILAN:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_MULTIFINANCE);
                break;
            case Constant.INTERNET:
                mPresenter.getServices(Constant.SERVICE_PAYMENT, "", no.getText().toString(), Constant.SERVICE_ISP);
                break;
            default:
                break;
        }
    }

    private void setListProduct(int position) {
        MiretaPOSApplication app = (MiretaPOSApplication) getApplication();
        FirebaseAnalytics mFirebaseAnalytics = app.getFirebaseAnalytics();
        Bundle params = new Bundle();
        pos = position;
        switch (position) {
            case Constant.INTERNET_DATA:
                setToolbarTitle("Data");
                jenisNomor.setText("Nomor");
                pricePurchase.setText("Pilih Paket");
                infoPurchase.setText("Pilih Paket");
                no.setHint("08XXXXXXXXXX");
                isUsingInquiry = false;
                quotaAdapter = new RecyPurchaseQuotaAdapter(false);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase Data");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase Data");
                nextBtn.setText("BELI");
                break;
            case Constant.ASURANSI:
                setToolbarTitle("Asuransi");
                no.setHint("Nomor tagihan asuransi");
                nextBtn.setText("CEK TAGIHAN");
                quotaAdapter = new RecyPurchaseQuotaAdapter(true);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase Asuransi");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase Asuransi");
                //inputAmount.setVisibility(View.VISIBLE);
                break;
            case Constant.BAZNAS:
                break;
            case Constant.BPJS_KESEHATAN:
                iconPurchase.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_bpjs_));
                setToolbarTitle("BPJS");
                no.setHint("1234567890");
                pickService.setVisibility(View.GONE);
                nextBtn.setText("CEK TAGIHAN");
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase BPJS");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase BPJS");
                break;
            case Constant.BPJS_KETENAGAKERJAAN:
                break;
            case Constant.LISTRIK:
                no.setHint("1234567890");
                iconPurchase.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_pln));
                if (getIntent().getIntExtra(TYPE_PLN, 0) == TOKEN_PLN) {
                    setToolbarTitle("TOKEN LISTRIK");
                    nextBtn.setText("ISI TOKEN");
                    quotaAdapter = new RecyPurchaseQuotaAdapter(false);
                    quotaAdapter.setListener(this);
                    listPurchase.setAdapter(quotaAdapter);
                    isTokenPln = true;
                } else {
                    setToolbarTitle("BAYAR LISTRIK");
                    pickService.setVisibility(View.GONE);
                    nextBtn.setText("CEK TAGIHAN");
                }
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase Listrik");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase Listrik");
                break;
            case Constant.PULSA_HANDPHONE:
                setToolbarTitle("PULSA");
                jenisNomor.setText("Nomor");
                no.setHint("08XXXXXXXXXX");
                nextBtn.setText("BELI");
                isUsingInquiry = false;
                quotaAdapter = new RecyPurchaseQuotaAdapter(false);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase pulsa");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase pulsa");
                break;
            case Constant.PASCABAYAR:
                setToolbarTitle("Handphone Pascabayar");
                listPurchase.setVisibility(View.GONE);
                nextBtn.setText("CEK TAGIHAN");
                break;
            case Constant.PDAM:
                iconPurchase.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_pdam));
                setToolbarTitle("PDAM");
                pricePurchase.setText("Pilih Daerah");
                infoPurchase.setText("Pilih Daerah");
                no.setHint("1234567890");
                nextBtn.setText("CEK TAGIHAN");
                quotaAdapter = new RecyPurchaseQuotaAdapter(true);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase PDAM");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase PDAM");
                break;
            case Constant.TV_INTERNET:
                setToolbarTitle("TV Kabel");
                pricePurchase.setText("Pilih Provider");
                infoPurchase.setText("Pilih Provider");
                nextBtn.setText("CEK TAGIHAN");
                no.setHint("1234567890");
                quotaAdapter = new RecyPurchaseQuotaAdapter(true);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase TV");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase TV");
                //inputAmount.setVisibility(View.VISIBLE);
                break;
            case Constant.CICILAN:
                setToolbarTitle("Cicilan");
                pricePurchase.setText("Pilih Installment");
                infoPurchase.setText("Pilih Installment");
                no.setHint("1234567890");
                nextBtn.setText("CEK TAGIHAN");
                quotaAdapter = new RecyPurchaseQuotaAdapter(true);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase Cicilan");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase Cicilan");
                //inputAmount.setVisibility(View.VISIBLE);
                break;
            case Constant.INTERNET:
                setToolbarTitle("Internet");
                pricePurchase.setText("Pilih Provider");
                infoPurchase.setText("Pilih Provider");
                no.setHint("1234567890");
                nextBtn.setText("CEK TAGIHAN");
                quotaAdapter = new RecyPurchaseQuotaAdapter(true);
                quotaAdapter.setListener(this);
                listPurchase.setAdapter(quotaAdapter);
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, "purchase Internet");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
                mFirebaseAnalytics.setUserProperty("Page", "purchase Internet");
                //inputAmount.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


    }


    @Override
    public void showProgressLoading() {
        String textLoading = "Mohon tunggu beberapa saat, no tagihan anda sedang dalam proses pengecekan";
        progressBar.show(this, textLoading, false, null);
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
        if (pos == Constant.INTERNET_DATA || pos == Constant.PULSA_HANDPHONE) {
            isPicked = false;
            infoPurchase.setText("Pilih Nominal");
            pricePurchase.setVisibility(View.GONE);
            listServices = null;
        }
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    public void onSuccessGetService(List<GServices> listServices) {
        this.listServices = listServices;
        isPicked = false;
        switch (pos) {
            case Constant.INTERNET_DATA:
                quotaAdapter.setData(listServices);
                pricePurchase.setVisibility(View.GONE);
                break;
            case Constant.ASURANSI:
                quotaAdapter.setData(listServices);
                break;
            case Constant.BAZNAS:
                break;
            case Constant.BPJS_KESEHATAN:
                isPicked = true;
                positionService = 0;
                break;
            case Constant.BPJS_KETENAGAKERJAAN:
                break;
            case Constant.LISTRIK:
                if (isTokenPln) {
                    quotaAdapter.setData(listServices);
                } else {
                    isPicked = true;
                    positionService = 0;
                }
                break;
            case Constant.PULSA_HANDPHONE:
                quotaAdapter.setData(listServices);
                pricePurchase.setVisibility(View.GONE);
                break;
            case Constant.PASCABAYAR:
                isPicked = true;
                positionService = 0;
                break;
            case Constant.PDAM:
                quotaAdapter.setData(listServices);
                break;
            case Constant.TV_INTERNET:

                quotaAdapter.setData(listServices);
                break;
            case Constant.CICILAN:
                quotaAdapter.setData(listServices);
                break;
            case Constant.INTERNET:
                quotaAdapter.setData(listServices);
                break;
            default:
                break;
        }
        if (listServices != null && listServices.size() > 0) {
            if (containerList.isShown()) {
                containerList.setVisibility(View.GONE);
                //nextBtn.setVisibility(View.VISIBLE);
            } else {
                containerList.setVisibility(View.VISIBLE);
                //nextBtn.setVisibility(View.GONE);
            }
            if(pos == Constant.LISTRIK){
                if (!isTokenPln) {
                    containerList.setVisibility(View.GONE);
                }
            }
        }else{
            MethodUtil.showCustomToast(PurchaseActivity.this, "Gagal mendapatkan data service", R.drawable.ic_error_login);
        }
    }

    @Override
    public void onSuccessInquiry(GTransaction transaction) {
        String info = transaction.service.name;
        String urlImage = "";
        try {
            switch (pos) {
                case Constant.INTERNET_DATA:
                    info = transaction.service.name;
                    if (transaction.service != null && transaction.service.provider != null && transaction.service.provider.logo != null) {
                        GLogo logo = transaction.service.provider.logo;
                        urlImage = logo.base_url + "/" + logo.path;
                    }
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.INTERNET_DATA, transaction.default_price, urlImage, drawableOperator);
                    break;
                case Constant.ASURANSI:
                    info = transaction.service.name;
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.ASURANSI, transaction.default_price, "", 0);
                    break;
                case Constant.BAZNAS:
                    break;
                case Constant.BPJS_KESEHATAN:
                    JSONObject objBPJS = new JSONObject(transaction.data);
                    if (objBPJS.has("customer_id")) {
                        info = "Pembayaran BPJS dengan no " + objBPJS.get("customer_id").toString() +
                                " atas nama " + objBPJS.get("customer_name").toString();
                    } else if (objBPJS.has("namapelanggan")) {
                        info = "Pembayaran BPJS dengan no " + objBPJS.get("nova").toString() +
                                " atas nama " + objBPJS.get("namapelanggan").toString();
                    }

                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.BPJS_KESEHATAN, transaction.default_price, "", 0);
                    break;
                case Constant.BPJS_KETENAGAKERJAAN:
                    break;
                case Constant.LISTRIK:
                    String transData = transaction.data.replace("/", " ").replace("\\", "").replace("\\\\", "")
                            .replace("\\\\\\", "").replace("\"{", "").replace("}\"", "");
                    JSONObject json = new JSONObject(transData);
                    if (isTokenPln) {
                        if (json.has("price")) {
                            info = "Token PLN " + json.get("price").toString() + " ke " + json.get("meter_serial").toString();
                        } else if (json.has("idmeter")) {
                            info = "Token PLN " + json.get("idmeter").toString() + " atas nama " + json.get("nama").toString() + " \n" +
                            "Daya : " + json.get("daya").toString().replaceFirst("^0+(?!$)", "");
                        }
                        gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.LISTRIK, transaction.default_price, "", 0);
                    } else {
                        if (json.has("customer_id")) {
                            info = "Pembayaran PLN dengan no " + json.get("customer_id").toString() + " atas nama " + json.get("customer_name").toString()
                                    + " untuk bulan " + json.get("bill_period").toString() + ", kWh : " + json.get("fare_power").toString();
                        } else if (json.has("idpel")) {
                            String bulan = json.get("blth1").toString();
                            bulan = bulan.substring(0,4) + "-" + bulan.substring(4, bulan.length());
                            info = "Pembayaran PLN dengan no " + json.get("idpel").toString() + " atas nama " + json.get("nama").toString()
                                    + " untuk bulan " + bulan + ", kWh : " + json.get("daya").toString().replaceFirst("^0+(?!$)", "");
                        } else {
                            info = "Pembayaran Listrik";
                        }

                        gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.LISTRIK, transaction.default_price, "", 0);
                    }
                    break;
                case Constant.PULSA_HANDPHONE:
                    info = "Pulsa " + MethodUtil.toCurrencyFormat(transaction.amount);
                    if (transaction.service != null && transaction.service.provider != null && transaction.service.provider.logo != null) {
                        GLogo logo = transaction.service.provider.logo;
                        urlImage = logo.base_url + "/" + logo.path;
                    }
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.PULSA_HANDPHONE, transaction.default_price, urlImage, drawableOperator);
                    break;
                case Constant.PASCABAYAR:
                    info = transaction.customer_no;
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.PASCABAYAR, transaction.default_price, "", 0);
                    break;
                case Constant.PDAM:
                    JSONObject obj = new JSONObject(transaction.data);
                    if (obj.has("provider_code")) {
                        info = "Pembayaran PDAM dengan no " + obj.get("customer_id").toString() + " atas nama " + obj.get("customer_name").toString() + "\n" +
                                "Periode : " + obj.get("bill_date").toString() + "\n" +
                                "Tagihan : " + obj.get("bill_amount").toString().replaceFirst("^0+(?!$)", "");
                    } else if (obj.has("customer_id")) {
                        info = "Pembayaran PDAM dengan no " + obj.get("customer_id").toString() + " atas nama " + obj.get("customer_name").toString() + "\n" +
                        "Periode : " + obj.get("bill_period").toString() + "\n" +
                        "Tagihan : " + obj.get("bill_amount").toString().replaceFirst("^0+(?!$)", "");
                    } else if (obj.has("data")) {
                        JSONObject dataPDAM = new JSONObject(obj.getJSONObject("data").toString());
                        info = "Pembayaran PDAM dengan no " + dataPDAM.get("customer_id").toString() +
                                " atas nama " + dataPDAM.get("customer_name").toString();
                    }else  {
                        info = "Pembayaran PDAM dengan no " + obj.get("idpel").toString() + " atas nama " + obj.get("namapelanggan").toString() + "\n" +
                                "Alamat : " + obj.get("alamat").toString() + "\n" +
                                "Tagihan : " + obj.get("totaltagihan").toString().replaceFirst("^0+(?!$)", "");
                    }

                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.PDAM, transaction.default_price, "", 0);
                    break;
                case Constant.TV_INTERNET:
                    info = transaction.service.name;
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.TV_INTERNET, transaction.default_price, "", 0);
                    break;
                case Constant.CICILAN:
                    info = transaction.service.name;
                    gotoTransactionReview(no.getText().toString(), info, transaction.id, Constant.CICILAN, transaction.default_price, "", 0);
                    break;
                case Constant.INTERNET:
                    info = transaction.service.name;
                    JSONObject objJS = new JSONObject(transaction.data);
                    JSONObject objData = new JSONObject(objJS.get("data").toString());
                    gotoTransactionReview(no.getText().toString() + " atas nama " +objData.get("customer_name").toString(), info, transaction.id, Constant.INTERNET, transaction.default_price, "", 0);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onListClick(int position) {
        containerList.setVisibility(View.GONE);
        //nextBtn.setVisibility(View.VISIBLE);
        GServices services = listServices.get(position);
        positionService = position;
        isPicked = true;
        switch (pos) {
            case Constant.INTERNET_DATA:
                pricePurchase.setText("Rp " + MethodUtil.toCurrencyFormat(services.default_price));
                pricePurchase.setVisibility(View.VISIBLE);
                infoPurchase.setText(services.name);
                break;
            case Constant.ASURANSI:
                infoPurchase.setText(services.name);
                break;
            case Constant.BAZNAS:
                break;
            case Constant.BPJS_KESEHATAN:
                break;
            case Constant.BPJS_KETENAGAKERJAAN:
                break;
            case Constant.LISTRIK:
                if (isTokenPln) {
                    infoPurchase.setText(services.name);
                } else {

                }
                break;
            case Constant.PULSA_HANDPHONE:
                pricePurchase.setText("Rp " + MethodUtil.toCurrencyFormat(services.default_price));
                pricePurchase.setVisibility(View.VISIBLE);
                infoPurchase.setText(services.name);
                break;
            case Constant.PASCABAYAR:
                break;
            case Constant.PDAM:
                infoPurchase.setText(services.name);
                break;
            case Constant.TV_INTERNET:
                inputAmount.setVisibility(((services.open_payment!=null)?(!services.open_payment.equals("0")?View.VISIBLE:View.GONE):View.GONE));
                infoPurchase.setText(services.name);
                break;
            case Constant.CICILAN:
                inputAmount.setVisibility(((services.open_payment!=null)?(!services.open_payment.equals("0")?View.VISIBLE:View.GONE):View.GONE));
                infoPurchase.setText(services.name);
                break;
            case Constant.INTERNET:
                inputAmount.setVisibility(((services.open_payment!=null)?(!services.open_payment.equals("0")?View.VISIBLE:View.GONE):View.GONE));
                infoPurchase.setText(services.name);
                break;
            default:
                break;
        }

    }
}
