package com.boardinglabs.mireta.standalone.modul.old.jiwasraya;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyPurchaseJiwasrayaAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
//import com.boardinglabs.mireta.smansa.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GJiwasrayaBillDetail;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya.TransactionReviewJiwasrayaActivity;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/23/17.
 */

public class JiwasrayaActivity extends BaseActivity implements
        CommonInterface,
        JiwasrayaView, RecyPurchaseJiwasrayaAdapter.Action {
    public static final String DATE_TRANSACTION = "dateTransaction";
    public static final String ORDER_ID = "orderId";
    public static final String NOTE = "note";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String DATA = "data";
    public static final String SERVICE_PROVIDER = "serviceProvider";

    private EditText textName;
    private TextView infoPurchase;
    private RecyPurchaseJiwasrayaAdapter jiwasrayaAdapter;
    private RecyclerView listPurchase;

    private GJiwasrayaBillDetail[] listServices;

    private RelativeLayout pickService;
    private Button terserah;
    private LinearLayout containerList;

    private JiwasrayaPresenter mPresenter;

    private String transactionsId;
    private String amounts;
    private String transactionBillCode;
    private String transactionBillName;

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
    protected int getLayoutResourceId() {
        return R.layout.jiwasraya_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("JIWASRAYA");
        textName = (EditText) findViewById(R.id.register_name);
        terserah = (Button) findViewById(R.id.next_btn);
        pickService = (RelativeLayout) findViewById(R.id.pick_service);
        containerList = (LinearLayout) findViewById(R.id.container_list_service);
        listPurchase = (RecyclerView) findViewById(R.id.purchase_list);
        listPurchase.setLayoutManager(new LinearLayoutManager(this));
        infoPurchase = (TextView) findViewById(R.id.info_purchase);
        initEvent();
    }

    private void initEvent() {
        RxView.clicks(pickService).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
            if(textName.getText().toString().length()>0) {
                processRequest();
            }else {
                MethodUtil.showCustomToast(JiwasrayaActivity.this, "Input nomor polis sebelum memilih service", R.drawable.ic_error_login);
            }
            }
        });

        textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>16){
                    MethodUtil.showCustomToast(JiwasrayaActivity.this, "Maximal input 16 karakter", R.drawable.ic_error_login);
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        });

    }

    private void processRequest() {
        mPresenter.setInquiry(Constant.SERVICE_JIWASRAYA, textName.getText().toString(),true, String.valueOf(0));
    }

        @Override
    protected void onCreateAtChild() {
        mPresenter = new JiwasrayaPresenterImpl(this, this);
        RxView.clicks(terserah).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(textName.getText().toString().length()>1) {
                    if (listServices != null) {
                        mPresenter.updateAmountInquiry(transactionsId, amounts);
                    } else {
                        MethodUtil.showCustomToast(JiwasrayaActivity.this, "Mohon lengkapi seluruh data", R.drawable.ic_error_login);
                    }
                }else {
                    MethodUtil.showCustomToast(JiwasrayaActivity.this, "nomor polis tidak boleh kosong", R.drawable.ic_error_login);
                }

            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (containerList.isShown()) {
            setToolbarTitle("JIWASRAYA");
            containerList.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void onSuccessEdit() {

    }

    @Override
    public void onListClick(int position) {
        containerList.setVisibility(View.GONE);
        terserah.setVisibility(View.VISIBLE);
        setToolbarTitle("JIWASRAYA");
        this.listServices = jiwasrayaAdapter.getDataService();
        GJiwasrayaBillDetail services = this.listServices[position];
        infoPurchase.setText(services.billName.replace("TOT:","") );
        this.amounts=services.billAmount;
        this.transactionBillCode=services.billCode;
        this.transactionBillName=services.billName;
    }

    @Override
    public void onSuccessInquiry(GTransaction transaction) throws JSONException {
        JSONObject objJiwasraya = new JSONObject(transaction.data);
        JSONObject objBIllDetails = new JSONObject(objJiwasraya.get("billDetails").toString());
        this.transactionsId=transaction.id;
        jiwasrayaAdapter = new RecyPurchaseJiwasrayaAdapter(false);
        jiwasrayaAdapter.setData(transaction);
        jiwasrayaAdapter.setListener(this);
        listPurchase.setAdapter(jiwasrayaAdapter);

        if (containerList.isShown()) {
            containerList.setVisibility(View.GONE);
            terserah.setVisibility(View.VISIBLE);
        } else {
            containerList.setVisibility(View.VISIBLE);
            setToolbarTitle("PAKET");
            terserah.setVisibility(View.GONE);
        }
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
        }
    }

    @Override
    public void onSuccessUpdateAmountInquiry(GTransaction transaction) throws JSONException {
        Intent intent = new Intent(this, TransactionReviewJiwasrayaActivity.class);
        intent.putExtra("transactionId", transaction.id);
        intent.putExtra("transactionData", transaction.data);
        intent.putExtra("transactionVendorPrice", transaction.vendor_price);
        intent.putExtra("transactionDefaultPrice", transaction.default_price);
        intent.putExtra("transactionOmzet", transaction.omzet);
        intent.putExtra("transactionBillCode", this.transactionBillCode);
        intent.putExtra("transactionBillName", this.transactionBillName);
        startActivity(intent);
    }
}
