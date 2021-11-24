package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.ListActionLoadMore;
import com.boardinglabs.mireta.standalone.component.adapter.RecyTopupAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAccounts;
import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.QuickResponse;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupvirtualaccount.VirtualAccountActivity;

import org.parceler.Parcels;

import java.util.List;

import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.CARD_TYPE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.MERCHANT_ID;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.NOTE;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.TOTAL_AMOUNT;
import static com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardActivity.VOUCHER_ID;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity.PAYMENT_ID;
import static com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupActivity.QUICK_RESPONSE;

/**
 * Created by Dhimas on 12/25/17.
 */

public class TopupTransactionFragment extends Fragment implements CommonInterface, TransactionView, RecyTopupAdapter.OnListClicked, ListActionLoadMore, BankUpdateInterface {
    private RecyclerView listView;
    private TransactionPresenter mPresenter;
    private List<GTransactionTopup> listItem;
    private RecyTopupAdapter mAdapter;
    private GTransactionTopup transactionTopup;
    private TransactionTopupResponse response;
    private RelativeLayout empty;

    public TopupTransactionFragment newInstance() {
        TopupTransactionFragment fragment = new TopupTransactionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment_layout, container, false);
        initComponent(view);
        mPresenter = new TransactionPresenterImpl(this, this);
        mPresenter.getTopup();
        return view;
    }

    private void initComponent(View view) {
        listView = (RecyclerView) view.findViewById(R.id.transaction_list);
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyTopupAdapter(this);
        mAdapter.setListenerOnClick(this);
        listView.setAdapter(mAdapter);

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgresLoading() {

    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            PreferenceManager.logOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onSuccessGetTransaction(List<GTransaction> response) {

    }

    @Override
    public void onSuccessGetTopup(List<GTransactionTopup> response) {
        listItem = response;
        mAdapter.addData(response);
        if (response.size() < 1) {
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessGetCashback(List<GCashbackAgent> response) {

    }


    @Override
    public void hideProgressList() {
        mAdapter.removeLoadingList();
    }

    @Override
    public void listClick(GTransactionTopup transactionTopup) {
        this.transactionTopup = transactionTopup;
        response = new TransactionTopupResponse();

        String[] dateTime = MethodUtil.formatDateAndTime(transactionTopup.created_at);
        response.setDate(dateTime[0]);
        response.setTime(dateTime[1]);
        String amountCharge;
        if (TextUtils.isEmpty(transactionTopup.amount_charged)) {
            amountCharge = transactionTopup.amount;
        } else {
            amountCharge = transactionTopup.amount_charged;
        }
        String adminVa = "0";
        if (transactionTopup.method_payment.equalsIgnoreCase("4")) {
            adminVa = transactionTopup.bank != null && !TextUtils.isEmpty(transactionTopup.bank.va_fee) ? transactionTopup.bank.va_fee : "0";
        }
        String uniqueAmount = !TextUtils.isEmpty(transactionTopup.unique_amount) ? transactionTopup.unique_amount : "0";
        int totalAmount = Integer.parseInt(amountCharge) + Integer.parseInt(uniqueAmount) + Integer.parseInt(adminVa);

        response.setInfo("Top up saldo Rp " + MethodUtil.toCurrencyFormat(totalAmount + ""));
        response.setOrderId(transactionTopup.id);
        response.setBankId(transactionTopup.bank_id);
        response.setAccountId(transactionTopup.account_id);
        response.setTopupSaldo(MethodUtil.toCurrencyFormat(totalAmount + ""));
        response.setStatus(transactionTopup.status);
        if (transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS)) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }
        response.setExpiredAt(transactionTopup.expired_at);
        response.setCreateAt(transactionTopup.created_at);

        response.setStatus(transactionTopup.status);
        response.setFromHome(false);
        if (transactionTopup.account != null && !transactionTopup.method_payment.equalsIgnoreCase("4") /*&& !transactionTopup.method_payment.equalsIgnoreCase("1")*/) {
            response.setBankName(transactionTopup.account.account_name);
            response.setBankAccount(transactionTopup.account.account_no);
            gotoStatusActivity();
        }else if (transactionTopup.sub_customer != null) {
            long temp = Long.parseLong(transactionTopup.balance_before) - Long.parseLong(transactionTopup.balance_after);
            String notes = !TextUtils.isEmpty(transactionTopup.notes) ? transactionTopup.notes : "";
            response.setNotes(notes);
            if (temp > 0) {
                response.setInfo("Kirim saldo Rp " + MethodUtil.toCurrencyFormat(totalAmount + "") + " ke " + transactionTopup.sub_customer.name + " " + transactionTopup.sub_customer.mobile);
            } else {
                response.setInfo("Terima saldo Rp " + MethodUtil.toCurrencyFormat(totalAmount + "") + " dari " + transactionTopup.sub_customer.name + " " + transactionTopup.sub_customer.mobile);
            }
            gotoStatusActivity();
        } else if (transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS) ||
                transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) {
            gotoStatusActivity();
        } else if (transactionTopup.method_payment.equalsIgnoreCase("4")) {
            Intent intentVA = new Intent(getActivity(), VirtualAccountActivity.class);
            String admin = transactionTopup.bank != null && !TextUtils.isEmpty(transactionTopup.bank.va_fee) ? transactionTopup.bank.va_fee : "0";
            int total = Integer.parseInt(transactionTopup.amount) + Integer.parseInt(admin);
            intentVA.putExtra(VirtualAccountActivity.AMOUNT_VA, total + "");
            intentVA.putExtra(VirtualAccountActivity.EXPIRED_DATE, transactionTopup.expired_at);
            intentVA.putExtra(VirtualAccountActivity.IS_FROM_HISTORY, true);
            startActivity(intentVA);


        } else if (transactionTopup.method_payment.equalsIgnoreCase("1")) {
            QuickResponse quickResponse = new QuickResponse();
            quickResponse.setTotalAmount(transactionTopup.amount_charged);
            quickResponse.setPaymentId(transactionTopup.payment.id);
            quickResponse.setOrderId(transactionTopup.id);
            quickResponse.setCreateAt(transactionTopup.created_at);
            Intent intentCC = new Intent(getActivity(), InputCreditcardActivity.class);
            intentCC.putExtra(TOTAL_AMOUNT, transactionTopup.amount_charged);
            intentCC.putExtra(VOUCHER_ID, "");
//                                intentCC.putExtra(MERCHANT_ID, orderId.getText().toString());
            intentCC.putExtra(MERCHANT_ID, "");
            intentCC.putExtra(CARD_TYPE, "credit");
            intentCC.putExtra(NOTE, "topup " + transactionTopup.id);
            intentCC.putExtra(PAYMENT_ID, transactionTopup.payment.id);
            intentCC.putExtra(InputCreditcardActivity.IS_TOPUP, true);
            intentCC.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
            startActivity(intentCC);


        } else {
            Intent intent = new Intent(getActivity(), PaymentMethodTopupActivity.class);
            intent.putExtra(PaymentMethodTopupActivity.AMOUNT, transactionTopup.amount);
            intent.putExtra(PaymentMethodTopupActivity.UNIQUE, transactionTopup.unique_amount);
            intent.putExtra(PaymentMethodTopupActivity.ORDER_ID, transactionTopup.id);
            intent.putExtra(PaymentMethodTopupActivity.VOUCHER, transactionTopup.voucher_id);
            intent.putExtra(PaymentMethodTopupActivity.EXPIRED, transactionTopup.expired_at);
            startActivity(intent);
        }
    }

    private void gotoStatusActivity() {
        Intent intent ;
//        if (transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS) || transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) {
            intent = new Intent(getActivity(), StatusTopupActivity.class);
            response.setFromHome(true);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
//        } else {
//            intent = new Intent(getActivity(), TransferTopupActivity.class);
//            intent.putExtra(TransferTopupActivity.IS_HOME, true);
//            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
//        }
        startActivity(intent);
    }

    @Override
    public void onLoadMoreList() {
        mPresenter.loadNextTopup();
    }

    @Override
    public void onGetBank(GAccounts accounts) {
        response.setBankName(accounts.account_name);
        response.setBankAccount(accounts.account_no);
        gotoStatusActivity();
    }
}
