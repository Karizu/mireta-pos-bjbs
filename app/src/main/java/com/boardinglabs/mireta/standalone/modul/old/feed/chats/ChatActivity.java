package com.boardinglabs.mireta.standalone.modul.old.feed.chats;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyChatAdapter;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLog;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLogGroup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.checkpasscode.CheckPasscodeActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;
import com.boardinglabs.mireta.standalone.modul.old.requestbalance.RequestBalanceActivity;
import com.boardinglabs.mireta.standalone.modul.old.requestbalance.RequestBalancePresenter;
import com.boardinglabs.mireta.standalone.modul.old.requestbalance.RequestBalancePresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.requestbalance.RequestBalanceView;
import com.boardinglabs.mireta.standalone.modul.old.sendbalance.SendBalanceActivity;
import com.boardinglabs.mireta.standalone.modul.old.sendbalance.SendBalancePresenter;
import com.boardinglabs.mireta.standalone.modul.old.sendbalance.SendBalancePresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.sendbalance.SendBalanceView;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements ListActionListener, ChatPresenter.ChatView, CommonInterface, SendBalanceView, RequestBalanceView {

    private RecyclerView chatList;
    private Button requestButton;
    private Button transferButton;

    private RecyChatAdapter chatAdapter;
    private String toCustomerId;
    private String toCustomerName;
    private String toAvatarBase64;
    private GCostumer toCustomer;
    private GCostumer currentCustomer;
    private ChatPresenter chatPresenter;
    private SendBalancePresenter mPresenter;
    private RequestBalancePresenter mRequestPresenter;

//    private GTransactionTopup transactionTopup;
    private TransactionTopupResponse response;

    private String toSendRequestId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void setContentViewOnChild() {
        toCustomerName = getIntent().getStringExtra("name");
        toCustomerId = getIntent().getStringExtra("to_customer_id");
        toAvatarBase64 = getIntent().getStringExtra("avatar_base64");
        initChat();
        mPresenter = new SendBalancePresenterImpl(this, this);
        mRequestPresenter = new RequestBalancePresenterImpl(this, this);
    }

    @Override
    protected void onCreateAtChild() {
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void goToDetail(GTransferRequestLog log){
        response = new TransactionTopupResponse();

        response.setJenisTransaksi(1);
        String[] dateTime = MethodUtil.formatDateAndTime(log.date);
        response.setDate(dateTime[0]);
        response.setTime(dateTime[1]);
        String amountCharge;
        if (TextUtils.isEmpty(log.amount)) {
            amountCharge = "0";
        } else {
            amountCharge = log.amount;
        }

        int totalAmount = Integer.parseInt(amountCharge);

        response.setOrderId(log.refference_id);
        response.setTopupSaldo(MethodUtil.toCurrencyFormat(totalAmount + ""));
        response.setStatus(Constant.TOPUP_STATUS_SUCCESS);
        response.setSuccess(true);



//        long temp = Long.parseLong(topupResponse.balance_before) - Long.parseLong(topupResponse.balance_after);
//        if (temp > 0) {
//            accountTitle.setText("PENERIMA");
//            statusInfo.setText("Transfer" + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
//        }else{
//            accountTitle.setText("PENGIRIM");
//            statusInfo.setText("Reques" + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
//        }

        if (toCustomer.id.equals(log.to_customer_id)) {
            response.setBalance_before("1");
            response.setBalance_after("0");
            response.setInfo("Kirim saldo Rp " + MethodUtil.toCurrencyFormat(totalAmount + "") + " ke " + toCustomer.name + " " + toCustomer.mobile);

            response.setCustomer_name(toCustomer.name);
            response.setCustomer_no(toCustomer.mobile);
            response.setCustomer_avatar(toCustomer.avatar_base64);

            response.setSub_customer_name(toCustomer.name);
            response.setSub_customer_no(toCustomer.mobile);
            response.setSub_customer_avatar(toCustomer.avatar_base64);
        }
        else{
            response.setBalance_before("0");
            response.setBalance_after("1");
            response.setInfo("Terima saldo Rp " + MethodUtil.toCurrencyFormat(totalAmount + "") + " dari " + toCustomer.name + " " + toCustomer.mobile);

            response.setCustomer_name(toCustomer.name);
            response.setCustomer_no(toCustomer.mobile);
            response.setCustomer_avatar(toCustomer.avatar_base64);

            response.setSub_customer_name(toCustomer.name);
            response.setSub_customer_no(toCustomer.mobile);
            response.setSub_customer_avatar(toCustomer.avatar_base64);
        }

        response.setFromHome(false);
        gotoStatusActivity();
    }

    private void gotoStatusActivity() {
        Intent intent ;
        intent = new Intent(ChatActivity.this, StatusTopupActivity.class);
        response.setFromHome(true);
        intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(response));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatPresenter != null){
            chatPresenter.getLogs(toCustomerId);
        }
    }

    private void initChat() {
        chatPresenter = new ChatPresenter(this,this);
        chatList = (RecyclerView) findViewById(R.id.messages_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(layoutManager);
        requestButton = (Button) findViewById(R.id.request_btn);
        transferButton = (Button) findViewById(R.id.transfer_button);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatActivity.this, RequestBalanceActivity.class);
                intent.putExtra("dest_cust_id", toCustomer.id);
                intent.putExtra("dest_cust_phone_number", toCustomer.mobile);
                intent.putExtra("dest_cust_name", toCustomer.name);
                intent.putExtra("dest_cust_avatar", getIntent().getStringExtra("avatar_base64"));
                intent.putExtra("is_back", "true");
                startActivity(intent);
            }
        });
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, SendBalanceActivity.class);
                intent.putExtra("dest_cust_id", toCustomer.id);
                intent.putExtra("dest_cust_phone_number", toCustomer.mobile);
                intent.putExtra("dest_cust_name", toCustomer.name);
                intent.putExtra("dest_cust_avatar", getIntent().getStringExtra("avatar_base64"));
                intent.putExtra("is_back", "true");
                startActivity(intent);
            }
        });

        if (toCustomer == null) {
            toCustomer = new GCostumer();
            toCustomer.id = toCustomerId;
            toCustomer.name = toCustomerName;
        }
        chatAdapter = new RecyChatAdapter(toCustomer);
        chatAdapter.setDataList(new ArrayList<GTransferRequestLog>());
        chatAdapter.setListener(this);

        chatList.setAdapter(chatAdapter);
        refreshChat();
    }

    @Override
    protected void refreshChat() {
        super.refreshChat();
        chatPresenter.getLogs(toCustomerId);
    }

    @Override
    public void itemClicked(int position) {
        GTransferRequestLog log = chatAdapter.feedList.get(position);
        if (log.status.equals("5")){
            goToDetail(log);
        }
        else{
            if (log.accepted_status.equals("0")){
                toSendRequestId = log.refference_id;
                Intent intent = new Intent(ChatActivity.this, CheckPasscodeActivity.class);
                String[] user = PreferenceManager.getUserInfo();
                GAgent agent = PreferenceManager.getAgent();
                String phone = "0";
                if (user != null && user[1] != null) {
                    phone = user[1];
                } else if (agent != null && agent.mobile != null) {
                    phone = agent.mobile;
                }
                intent.putExtra(OtpActivity.MOBILE, phone);
                startActivityForResult(intent, 1);
            }
        }
//        GTransferRequestLog transferRequestLog = chatAdapter.feedList.get(position);
//        if (transferRequestLog.activity.equals("request")){
//            Intent intent = new Intent(this, SendBalanceActivity.class);
//            intent.putExtra("dest_cust_id", transferRequestLog..id);
//            intent.putExtra("dest_cust_phone_number", selectedContact.mobile);
//            intent.putExtra("dest_cust_name", selectedContact.name);
//            intent.putExtra("dest_cust_avatar", selectedContact.avatar_base64);
//            startActivity(intent);
//        }
//        else{
//
//        }

    }

    @Override
    public void itemDeleted(int position) {
        GTransferRequestLog log = chatAdapter.feedList.get(position);
        if (log.accepted_status.equals("0")){
            mRequestPresenter.rejectRequest(log.refference_id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.sendBalanceByRequest(toSendRequestId);
        }
    }


    @Override
    public void showProgressLoading() {
//        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
//        progressBar.getDialog().dismiss();
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
    public void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups) {

    }

    @Override
    public void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer) {
        toCustomer = to_customer;
        currentCustomer = customer;
        chatAdapter.setDataList(logs);
    }

    @Override
    public void onFailureGetTransferRequestLogGroups(String message) {

    }

    @Override
    public void onSuccessSendBalance() {
        MethodUtil.showCustomToast(this, "Transfer sukses", R.drawable.ic_check_circle);
        refreshChat();
    }

    @Override
    public void onSuccessRequestBalance() {
        MethodUtil.showCustomToast(this, "Tolak request sukses", R.drawable.ic_check_circle);
        refreshChat();
    }
}
