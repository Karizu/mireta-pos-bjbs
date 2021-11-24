package com.boardinglabs.mireta.standalone.modul.old.feed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.ListActionLoadMore;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.PaymentFragment;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import com.boardinglabs.mireta.standalone.component.adapter.RecyFeedTrxAdapter;

public class FeedTrxFragment  extends Fragment implements com.boardinglabs.mireta.standalone.modul.CommonInterface, TransactionView, RecyFeedTrxAdapter.OnListClicked, ListActionLoadMore {
    private RecyclerView listView;
    private PaymentFragment paymentFragment;
    private RecyFeedTrxAdapter mAdapter;
    private CustomProgressBar progressBar = new CustomProgressBar();
    private com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction.TransactionPresenter mPresenter;
    private RelativeLayout empty;
    private GTransactionTopup transactionTopup;
    private TransactionTopupResponse response;
    private ImageView emptyImg;
    private TextView emptyText;
    private SwipeRefreshLayout pullToRefresh;
    private Button topupFilterButton;
    private Button serviceFilterButton;
    private Button ppobFilterButton;
    private static String IS_PPOB = "isPPob";
    private boolean isPPOB;
    private boolean isTopUp;

    public void openFilter(){
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        CharSequence items[] = new CharSequence[] {"PPOB", "Service", "TopUp"};
        int selected = 0;
        if (isPPOB) {
            selected = 0;
        }
        else if(isTopUp) {
            selected = 2;
        } else {
            selected = 1;
        }

        adb.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    isPPOB = true;
                    isTopUp = false;
                }
                else if (which == 1){

                    isPPOB = false;
                    isTopUp = false;
                }
                else if (which == 2){

                    isPPOB = false;
                    isTopUp = true;
                }
                dialog.dismiss();
                pullToRefresh.setRefreshing(true);
                refreshData();
            }
        });
        adb.setNegativeButton("Tutup", null);
        adb.setTitle("Filter Transaksi");

        adb.show();
    }

    public FeedTrxFragment newInstance(boolean isPPOB) {
        FeedTrxFragment fragment = new FeedTrxFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_PPOB, isPPOB);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FeedTrxFragment newInstance(PaymentFragment paymentFragment) {
        FeedTrxFragment fragment = new FeedTrxFragment();
        fragment.paymentFragment = paymentFragment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_trx, container, false);
        initComponent(view);
        //isPPOB = getArguments().getBoolean(IS_PPOB);
        isPPOB = false;
        isTopUp = true;
        mPresenter = new TransactionPresenterImpl(this, this);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                refreshData();
            }
        });

        pullToRefresh.setRefreshing(true);
        refreshData();
        return view;
    }

    private void refreshData(){
        showProgressLoading();
        if (isPPOB) {
            mPresenter.getTransactionPPOB();
        }
        else if(isTopUp) {
            mPresenter.getTopup();
        } else {
            mPresenter.getTransaction();
        }
    }

    private void initComponent(View view) {
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        emptyImg = (ImageView) view.findViewById(R.id.img_empty);
        emptyText = (TextView) view.findViewById(R.id.text_empty);
        listView = (RecyclerView) view.findViewById(R.id.transaction_list);
        mAdapter = new RecyFeedTrxAdapter(this, isPPOB);
        mAdapter.setListenerOnClick(this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(mAdapter);
        ppobFilterButton = (Button) view.findViewById(R.id.ppobFilterButton);
        topupFilterButton =  (Button) view.findViewById(R.id.topupFilterButton);
        serviceFilterButton = (Button) view.findViewById(R.id.serviceFilterButton);

        ppobFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPPOB = true;
                isTopUp = false;
                ppobFilterButton.setBackgroundResource(R.drawable.border_bottom_blue);
                topupFilterButton.setBackgroundResource(R.color.white);
                serviceFilterButton.setBackgroundResource(R.color.white);
                pullToRefresh.setRefreshing(true);
                refreshData();
            }
        });

        topupFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPPOB = false;
                isTopUp = true;
                ppobFilterButton.setBackgroundResource(R.color.white);
                topupFilterButton.setBackgroundResource(R.drawable.border_bottom_blue);
                serviceFilterButton.setBackgroundResource(R.color.white);
                pullToRefresh.setRefreshing(true);
                refreshData();
            }
        });

        serviceFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPPOB = false;
                isTopUp = false;
                ppobFilterButton.setBackgroundResource(R.color.white);
                topupFilterButton.setBackgroundResource(R.color.white);
                serviceFilterButton.setBackgroundResource(R.drawable.border_bottom_blue);
                pullToRefresh.setRefreshing(true);
                refreshData();
            }
        });
    }



    @Override
    public void showProgressLoading() {
        progressBar.show(getContext(), "", false, null);
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
        hideProgresLoading();
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
        hideProgresLoading();
        pullToRefresh.setRefreshing(false);
        mAdapter.add(response);
        showEmptyholder(mAdapter.getItemCount()==0);
    }

    @Override
    public void onSuccessGetTopup(List<GTransactionTopup> response) {
        hideProgresLoading();
        pullToRefresh.setRefreshing(false);
    List<GTransaction> transactions = new ArrayList<>();
        for (GTransactionTopup topUp : response){
            try{
                GTransaction trx = new GTransaction();
                trx.id = topUp.id;
                trx.default_price = topUp.amount;
                trx.amount_charged = topUp.amount;
                trx.status_label = topUp.status_label;
                trx.created_at = topUp.created_at;
                trx.topup = topUp;
                transactions.add(trx);
            }
            catch (Exception e){
                
            }
        }
        mAdapter.add(transactions);
        showEmptyholder(mAdapter.getItemCount()==0);
    }

    private void showEmptyholder(boolean show){
        if(show) {
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            listView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
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
    public void listClick(GTransaction transaction) {
        if (isPPOB) {
            TransactionTopupResponse response = new TransactionTopupResponse();
            response.setFail(false);
            response.setJenisTransaksi(3);
            String service = transaction.service != null ? transaction.service.name : transaction.notes;
            String note = service + ", " + transaction.customer_no;
//            int service = Integer.parseInt(transaction.service.category);
            switch (transaction.service.category) {
                case Constant.SERVICE_PLN:
                    if (!TextUtils.isEmpty(transaction.data)) {
                        try {
                            JSONObject json = new JSONObject(transaction.data);
                            if (json.has("harga")) {
                                String token = json.get("token").toString();
                                note = "Token PLN " + Integer.parseInt(json.get("harga").toString()) + " ke " +
                                        json.get("idmeter") + " SUKSES. SN: " +
                                        json.get("token").toString() + "/" +
                                        json.get("nama").toString() +
                                        "/" + json.get("kwh").toString().replaceFirst("^0+(?!$)", "");
                            } else {
                                String token = json.get("token").toString();
                                note = "Token PLN " + json.get("price").toString() + " ke " +
                                        json.get("meter_serial") + " SUKSES. SN: " +
                                        MethodUtil.formatTokenNumber(token) + "/" +
                                        json.get("customer_name").toString() +
                                        "/" + json.get("total_kwh").toString();
                            }
                            response.setCustomer_name(json.get("customer_name").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject json = new JSONObject(transaction.data);
                            note = "Pembayaran PLN dengan no " + json.get("customer_id").toString() + " atas nama " + json.get("customer_name").toString()
                                    + " untuk bulan " + json.get("bill_period").toString() + ", kWh : " + json.get("fare_power").toString();
                            response.setCustomer_name(json.get("customer_name").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constant.SERVICE_BPJS:
                    try {
                        JSONObject json = new JSONObject(transaction.data);
                        note = "Pembayaran BPJS dengan no " + json.get("customer_id").toString() +
                                " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        response.setCustomer_name(json.get("customer_name").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.SERVICE_PDAM:
                    try {
                        JSONObject json = new JSONObject(transaction.data);
                        if (!TextUtils.isEmpty(json.get("provider_code").toString())) {
                            note = "Pembayaran PAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        } else if (!TextUtils.isEmpty(json.get("idpel").toString())) {
                            note = "Pembayaran PDAM dengan no " + json.get("idpel").toString() +
                                    " atas nama " + json.get("namapelanggan").toString() + " Sukses.";
                        } else {
                            note = "Pembayaran PDAM dengan no " + json.get("customer_id").toString() +
                                    " atas nama " + json.get("customer_name").toString() + " Sukses.";
                        }
                        response.setCustomer_name(json.get("customer_name").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            response.setDate(dateTime[0]);
            response.setTime(dateTime[1]);
            response.setSuccess(true);
            response.setOrderId(transaction.id);
            response.setInfo(note);
            response.setTopupSaldo(transaction.default_price);
            response.setFromHome(true);
            if(transaction.status!=null){
//                Log.i("kunam", transaction.status);
                response.setStatusTrx(transaction.status);
            }else{
//                Log.i("kunam", transaction.status_label);
                response.setStatusTrx(transaction.status_label);
            }
            response.setCustomer_no(transaction.customer_no);
            response.setService_category(transaction.service.category);
            response.setService_name(transaction.service.name);

            Intent intent = new Intent(getActivity(), StatusTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
            startActivity(intent);
        }
        else if (isTopUp){
            this.transactionTopup = transaction.topup;
            response = new TransactionTopupResponse();
            response.setJenisTransaksi(1);
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

            if(transaction.topup!=null){
                response.setCustomer_name(transaction.topup.customer.name);
                response.setCustomer_no(transaction.topup.customer.mobile);
                response.setCustomer_avatar(transaction.topup.customer.avatar_base64);
                if(transaction.topup.sub_customer!=null && transaction.topup.balance_before!=null && transaction.topup.balance_after!=null){
                    response.setSub_customer_name(transaction.topup.sub_customer.name);
                    response.setSub_customer_no(transaction.topup.sub_customer.mobile);
                    response.setSub_customer_avatar(transaction.topup.sub_customer.avatar_base64);
                    response.setBalance_before(transaction.topup.balance_before);
                    response.setBalance_after(transaction.topup.balance_after);
                }
            }

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
//                Intent intentVA = new Intent(getActivity(), VirtualAccountActivity.class);
//                String admin = transactionTopup.bank != null && !TextUtils.isEmpty(transactionTopup.bank.va_fee) ? transactionTopup.bank.va_fee : "0";
//                int total = Integer.parseInt(transactionTopup.amount) + Integer.parseInt(admin);
//                intentVA.putExtra(VirtualAccountActivity.AMOUNT_VA, total + "");
//                intentVA.putExtra(VirtualAccountActivity.EXPIRED_DATE, transactionTopup.expired_at);
//                intentVA.putExtra(VirtualAccountActivity.IS_FROM_HISTORY, true);
//                startActivity(intentVA);
                gotoStatusActivity();

            } else if (transactionTopup.method_payment.equalsIgnoreCase("1")) {
//                QuickResponse quickResponse = new QuickResponse();
//                quickResponse.setTotalAmount(transactionTopup.amount_charged);
//                quickResponse.setPaymentId(transactionTopup.payment.id);
//                quickResponse.setOrderId(transactionTopup.id);
//                quickResponse.setCreateAt(transactionTopup.created_at);
//                Intent intentCC = new Intent(getActivity(), InputCreditcardActivity.class);
//                intentCC.putExtra(TOTAL_AMOUNT, transactionTopup.amount_charged);
//                intentCC.putExtra(VOUCHER_ID, "");
////                                intentCC.putExtra(MERCHANT_ID, orderId.getText().toString());
//                intentCC.putExtra(MERCHANT_ID, "");
//                intentCC.putExtra(CARD_TYPE, "credit");
//                intentCC.putExtra(NOTE, "topup " + transactionTopup.id);
//                intentCC.putExtra(PAYMENT_ID, transactionTopup.payment.id);
//                intentCC.putExtra(InputCreditcardActivity.IS_TOPUP, true);
//                intentCC.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
//                startActivity(intentCC);
                gotoStatusActivity();

            } else {
//                Intent intent = new Intent(getActivity(), PaymentMethodTopupActivity.class);
//                intent.putExtra(PaymentMethodTopupActivity.AMOUNT, transactionTopup.amount);
//                intent.putExtra(PaymentMethodTopupActivity.UNIQUE, transactionTopup.unique_amount);
//                intent.putExtra(PaymentMethodTopupActivity.ORDER_ID, transactionTopup.id);
//                intent.putExtra(PaymentMethodTopupActivity.VOUCHER, transactionTopup.voucher_id);
//                intent.putExtra(PaymentMethodTopupActivity.EXPIRED, transactionTopup.expired_at);
//                startActivity(intent);
                gotoStatusActivity();
            }
        }
        else {
            TransactionTopupResponse response = new TransactionTopupResponse();
            response.setFail(false);
            response.setJenisTransaksi(2);
            String[] dateTime = MethodUtil.formatDateAndTime(transaction.created_at);
            response.setDate(dateTime[0]);
            response.setTime(dateTime[1]);
            if (!TextUtils.isEmpty(transaction.status_label)) {
                switch (transaction.status_label) {
                    case "waiting payment":
                        response.setSuccess(false);
                        break;
                    case "success":
                        response.setSuccess(true);
                        break;
                    default:
                        response.setFail(true);
                        break;
                }
            } else {
                if (transaction.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS)) {
                    response.setSuccess(true);
                } else {
                    response.setSuccess(false);
                }
            }
            response.setOrderId(transaction.id);
            response.setInfo(transaction.notes);
            response.setMerchant_name(transaction.merchant_name);
            response.setTopupSaldo(transaction.amount_charged);
            response.setFromHome(true);
            Intent intent = new Intent(getActivity(), StatusTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
            startActivity(intent);
        }
    }

    private void gotoStatusActivity() {
        Intent intent ;
        if (transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_SUCCESS) ||
                transactionTopup.status.equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) {
            intent = new Intent(getActivity(), StatusTopupActivity.class);
            response.setFromHome(true);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
        } else {
            intent = new Intent(getActivity(), TransferTopupActivity.class);
            intent.putExtra(TransferTopupActivity.IS_HOME, true);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
        }
        startActivity(intent);
    }

    @Override
    public void onLoadMoreList() {
        if (isPPOB) {
            mPresenter.loadNextTransactionPPOB();
        } else if(isTopUp) {
            mPresenter.loadNextTopup();
        }else{
            mPresenter.loadNextTransaction();
        }

    }
}
