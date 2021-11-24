package com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyBanksAdapter;
import com.boardinglabs.mireta.standalone.component.fontview.RobotoBoldTextView;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GBanks;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TopupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.QuickResponse;
import com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupvirtualaccount.VirtualAccountActivity;

import org.parceler.Parcels;

import java.util.List;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.CARD_TYPE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.MERCHANT_ID;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.NOTE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.TOTAL_AMOUNT;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.VOUCHER_ID;

/**
 * Created by Dhimas on 9/29/17.
 */

public class PaymentMethodTopupActivity extends BaseActivity implements RecyBanksAdapter.OnClickItem, PaymentMethodTopupView, CommonInterface{
    public static final String AMOUNT = "amount";
    public static final String UNIQUE = "unique";
    public static final String ORDER_ID = "orderId";
    public static final String VOUCHER = "voucher";
    public static final String EXPIRED = "expired";
    public static final String IS_USING_CC = "isUsingCC";
    public static final String IS_USING_VA = "isUsingVA";
    public static final String QUICK_RESPONSE = "quickResponse";
    public static final String PAYMENT_ID = "paymentId";


    private RecyclerView listBank;
    private RecyBanksAdapter mAdapter;
    private LinearLayout containerReview;
    private LinearLayout dropdownBankList;
    private TextView topupText;
    private TextView infoTopup;
    private TextView orderIdText;
    private TextView amountTopup;
    private TextView uniqueTopup;
    private TextView voucherAmount;
    private TextView totalAmount;
    private PaymentMethodTopupPresenter mPresenter;
    private List<GBanks> banks;
    private Button nextBtn;
    private String bankId;
    private String accountId;
    private String amount;
    private String expired;
    private boolean isCC;
    private boolean isVA;
    private TextView fee;
    private String paymentId;
    private QuickResponse quickResponse;
    private String voucher = "0";
    private GBanks activeBank;
    private RobotoBoldTextView uniqueText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.method_payment_topup;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("DETAIL PEMBAYARAN");
        nextBtn = (Button) findViewById(R.id.next_btn);
        listBank = (RecyclerView) findViewById(R.id.list_bank);
        mAdapter = new RecyBanksAdapter(this);
        listBank.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listBank.setAdapter(mAdapter);
        containerReview = (LinearLayout) findViewById(R.id.container_review);
        dropdownBankList = (LinearLayout) findViewById(R.id.dropdown_bank_list);
        topupText = (TextView) findViewById(R.id.topup_saldo);
        infoTopup = (TextView) findViewById(R.id.info_saldo);
        orderIdText = (TextView) findViewById(R.id.order_id_text);
        amountTopup = (TextView) findViewById(R.id.amount_topup);
        uniqueTopup = (TextView) findViewById(R.id.unique_topup);
        voucherAmount = (TextView) findViewById(R.id.amount_voucher_topup);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        fee = (TextView) findViewById(R.id.fee);
        uniqueText = (RobotoBoldTextView) findViewById(R.id.unique_text);

        amount = getIntent().getStringExtra(AMOUNT);
        paymentId = getIntent().getStringExtra(PAYMENT_ID);
        String unique = !TextUtils.isEmpty(getIntent().getStringExtra(UNIQUE)) ? getIntent().getStringExtra(UNIQUE) : "0";
        String orderId = getIntent().getStringExtra(ORDER_ID);
        voucher = getIntent().getStringExtra(VOUCHER);
        expired = getIntent().getStringExtra(EXPIRED);
        isVA = getIntent().getBooleanExtra(IS_USING_VA, false);
        isCC = getIntent().getBooleanExtra(IS_USING_CC, false);
        if (isCC || isVA) {
            dropdownBankList.setVisibility(View.GONE);
        }

        String topupText = "Rp " + MethodUtil.toCurrencyFormat(amount.replace(".", ""));
        String[] user = PreferenceManager.getUserInfo();

        orderIdText.setText(orderId);
        amountTopup.setText("Rp " +MethodUtil.toCurrencyFormat(amount));
        uniqueTopup.setText(MethodUtil.toCurrencyFormat(unique));
        String userSaldo = user != null && user[1] != null ? user[1] : "";
        infoTopup.setText(String.format(getResources().getString(R.string.pengisian_saldo), userSaldo));
        this.topupText.setText(topupText);
        if (!TextUtils.isEmpty(voucher)) {
            voucherAmount.setText("- " + MethodUtil.toCurrencyFormat(voucher));
        } else {
            voucherAmount.setText("0");
            voucher = "0";
        }
        int total;
        if (isCC) {
            quickResponse = Parcels.unwrap(getIntent().getParcelableExtra(QUICK_RESPONSE));
            quickResponse.setOrderId(orderId);
            total = Integer.parseInt(quickResponse.getTotalAmount());
            fee.setText(quickResponse.getFee());
            uniqueText.setText("Biaya Layanan");
        } else {
            amount = !TextUtils.isEmpty(amount) ? amount : "0";
            unique = !TextUtils.isEmpty(unique) ? unique : "0";
            voucher = !TextUtils.isEmpty(voucher) ? voucher : "0";
            total = Integer.parseInt(amount) + Integer.parseInt(unique) - Integer.parseInt(voucher);
        }

//        int total = Integer.parseInt(amount) - Integer.parseInt(voucher);
        totalAmount.setText("Rp " +MethodUtil.toCurrencyFormat(total + ""));
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PaymentMethodTopupPresenterImpl(this, this);
        mPresenter.requestBanks();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void onClick(int position) {
        for(int i= 0;i<banks.size();i++){
            banks.get(i).selected = false;
        }
        banks.get(position).selected = true;
        activeBank = banks.get(position);
        bankId = banks.get(position).id;
        if (banks.get(position).accounts != null) {
            accountId = banks.get(position).accounts.id;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initEvent() {
        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isCC) {
                    Intent intentCC = new Intent(PaymentMethodTopupActivity.this, InputCreditcardActivity.class);
                    intentCC.putExtra(TOTAL_AMOUNT, amount);
                    intentCC.putExtra(VOUCHER_ID, "");
                    intentCC.putExtra(MERCHANT_ID, "");
                    intentCC.putExtra(CARD_TYPE, "credit");
                    intentCC.putExtra(NOTE, "topup " + orderIdText.getText().toString());
                    intentCC.putExtra(PAYMENT_ID, paymentId);
                    intentCC.putExtra(InputCreditcardActivity.IS_TOPUP, true);
                    intentCC.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
                    startActivity(intentCC);
                } else if (isVA) {
                    mPresenter.confirmPayment(orderIdText.getText().toString(), bankId, accountId);
                } else {
                    if (TextUtils.isEmpty(bankId)) {
                        MethodUtil.showCustomToast(PaymentMethodTopupActivity.this, "Harap pilih metode bayar", R.drawable.ic_error_login);
                    } else {
                        mPresenter.confirmPayment(orderIdText.getText().toString(), bankId, accountId);
                    }
                }

            }
        });
    }

    @Override
    public void onSuccessRequest(TopupResponse response, String avatar, String mobile) {

        TransactionTopupResponse tResponse = new TransactionTopupResponse();
        for (GBanks bank : banks) {
            if (bank.id.equalsIgnoreCase(response.bank_id)) {
                tResponse.setBankAccount(bank.accounts.account_no);
                tResponse.setBankName(bank.accounts.account_name);
                tResponse.setBankId(bankId);
                tResponse.setAccountId(accountId);
            }
        }
        tResponse.setOrderId(orderIdText.getText().toString());
        tResponse.setTopupSaldo(totalAmount.getText().toString().replace(".", ""));
        String[] dateTime = MethodUtil.formatDateAndTime(response.created_at);
        tResponse.setDate(dateTime[0]);
        tResponse.setTime(dateTime[1]);
        tResponse.setInfo("Top up saldo " + amount.replace(".", ""));
        tResponse.setExpiredAt(response.expired_at);
        tResponse.setCreateAt(response.created_at);
        if (response.status == Integer.parseInt(Constant.TOPUP_STATUS_SUCCESS)) {
            tResponse.setSuccess(true);
        }
        if (isVA) {
            Intent intent = new Intent(this, VirtualAccountActivity.class);
            intent.putExtra(VirtualAccountActivity.EXPIRED_DATE, response.expired_at);
            intent.putExtra(VirtualAccountActivity.AMOUNT_VA, totalAmount.getText().toString().replace(".",""));
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(tResponse));
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TransferTopupActivity.class);
            intent.putExtra("bank_name", activeBank.name);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(tResponse));
            startActivity(intent);
        }

    }

    @Override
    public void onSuccessRequestBanks(List<GBanks> listBank) {
        banks = listBank;
        mAdapter.setData(listBank);
        if (isVA && listBank != null) {
            for (GBanks bank : listBank) {
                if (!TextUtils.isEmpty(bank.va_fee)) {
                    fee.setText(MethodUtil.toCurrencyFormat(bank.va_fee));
                    bankId = bank.id;
                    accountId = bank.accounts.id;
                    voucher = !TextUtils.isEmpty(voucher) ? voucher : "0";
                    int total = Integer.parseInt(amount) + Integer.parseInt(bank.va_fee) - Integer.parseInt(voucher);
                    uniqueText.setText("Biaya Layanan");
                    uniqueTopup.setText(MethodUtil.toCurrencyFormat(bank.va_fee));
                    totalAmount.setText(MethodUtil.toCurrencyFormat(total + ""));
                }
            }
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
}
