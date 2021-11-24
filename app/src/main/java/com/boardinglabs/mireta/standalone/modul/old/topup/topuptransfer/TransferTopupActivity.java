package com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.MiretaPOSApplication;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GBanks;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TopupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.chat.ChatActivity;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupPresenter;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupPresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod.PaymentMethodTopupView;
import com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus.StatusTopupActivity;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;
import rx.functions.Action1;

/**
 * Created by Dhimas on 9/29/17.
 */

public class TransferTopupActivity extends BaseActivity implements PaymentMethodTopupView, CommonInterface {
    public static final String TOPUP_RESPONSE = "topupResponse";
    public static final String IS_HOME = "isHome";
    private TransactionTopupResponse topupResponse;
    private Button nextBtn;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private LinearLayout containerTimer;
    private LinearLayout containerExpiredTime;
    private PaymentMethodTopupPresenter mPresenter;
    private CountDownTimer countDownTimer;
    private boolean isFromHome;
    private ImageView imgTransfer;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.transfer_payment_topup;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Bank Transfer");
        nextBtn = (Button) findViewById(R.id.next_btn);

        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
        isFromHome = getIntent().getBooleanExtra(IS_HOME, false);
        TextView totalAmount = (TextView) findViewById(R.id.total_amount);
        TextView orderIdText = (TextView) findViewById(R.id.order_id_text);
        TextView accountName = (TextView) findViewById(R.id.account_name);
        TextView accountNumber = (TextView) findViewById(R.id.account_number);
        TextView time = (TextView) findViewById(R.id.time);
        TextView date = (TextView) findViewById(R.id.date);
        hour = (TextView) findViewById(R.id.jam);
        minute = (TextView) findViewById(R.id.menit);
        second = (TextView) findViewById(R.id.detik);
        containerTimer = (LinearLayout) findViewById(R.id.container_countdown);
        containerExpiredTime = (LinearLayout) findViewById(R.id.note_expired_time);
        imgTransfer = (ImageView) findViewById(R.id.img_transfer);
        ImageView bankLogo = (ImageView) findViewById(R.id.bank_logo);
        if(getIntent().getStringExtra("bank_name")!=null) {
            switch (getIntent().getStringExtra("bank_name")) {
                case "Bank Mandiri":
                    Glide.with(this).load(R.drawable.ic_bank_mandiri).dontAnimate().into(bankLogo);
                    break;
                case "Bank BCA":
                    Glide.with(this).load(R.drawable.ic_bank_bca).dontAnimate().into(bankLogo);
                    break;
                case "Bank Permata":
                    Glide.with(this).load(R.drawable.ic_bank_permata).dontAnimate().into(bankLogo);
                    break;
                case "Bank BNI":
                    Glide.with(this).load(R.drawable.ic_bank_bni).dontAnimate().into(bankLogo);
                    break;
                case "Bank UOB":
                    Glide.with(this).load(R.drawable.ic_bank_uob).dontAnimate().into(bankLogo);
                    break;
                case "Bank Woori Saudara":
                    Glide.with(this).load(R.drawable.ic_bank_woori_saudara).dontAnimate().into(bankLogo);
                    break;
                case "Bank Cimb Niaga":
                    Glide.with(this).load(R.drawable.ic_bank_cimb_niaga).dontAnimate().into(bankLogo);
                    break;
                default:
                    Glide.with(this).load(R.drawable.pampasy_logo).dontAnimate().into(bankLogo);
                    break;
            }
        }else{
            LinearLayout bankContainer = findViewById(R.id.bank_container);
            containerTimer.setVisibility(View.GONE);
            bankContainer.setVisibility(View.GONE);
            TextView failureText = findViewById(R.id.failure_text);
            failureText.setVisibility(View.VISIBLE);
            if(!topupResponse.isFail && !topupResponse.isSuccess) {
                failureText.setText("MENUNGGU");
                failureText.setTextColor(Color.parseColor("#E0D000"));
            }
        }
//        if (!PreferenceManager.getStatusAkupay()) {
            imgTransfer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pampasy_icon));
//        }
        totalAmount.setText("Rp "+ MethodUtil.toCurrencyFormat(topupResponse.getTopupSaldo()));
        orderIdText.setText(topupResponse.getOrderId());
        accountName.setText(topupResponse.getBankName());
        accountNumber.setText(topupResponse.getBankAccount());
        time.setText(topupResponse.getTime());
        date.setText(topupResponse.getDate());

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                Intent intent = new Intent(TransferTopupActivity.this, StatusTopupActivity.class);
//                topupResponse.setTopupTransaction(true);
//                intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
//                startActivity(intent);
                mPresenter.confirmPayment(topupResponse.getOrderId(), topupResponse.getBankId(), topupResponse.getAccountId());
            }
        });

        RxView.clicks(containerExpiredTime).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MiretaPOSApplication app = (MiretaPOSApplication) getApplication();
                Socket mSocket = app.getSocket();
                GAgent agent = PreferenceManager.getAgent();
                mSocket.emit("join room", agent.id);
                Intent intent = new Intent(TransferTopupActivity.this, ChatActivity.class);
                String[] user = PreferenceManager.getUserInfo();
                intent.putExtra("username", user[0]);
                intent.putExtra("numUsers", 2);
                startActivity(intent);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        Date endDate = null;
        final long endTime = System.currentTimeMillis();
        try {
            endDate = formatter.parse(topupResponse.getExpiredAt());
            endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final NumberFormat f = new DecimalFormat("00");
        if (endDate != null) {
            countDownTimer = new CountDownTimer(endDate.getTime(), 1000) {

                public void onTick(long millisUntilFinished) {
                    long diff = millisUntilFinished - endTime;
                    long seconds = (diff / 1000) % 60 ;
                    if (seconds < 0) {
                        finishCountdown();
                    }
                    second.setText(f.format(seconds));
                    long minutes = ((diff / (1000*60)) % 60);
                    minute.setText(f.format(minutes));
                    long hours   = ((diff / (1000*60*60)) % 24);
                    hour.setText(f.format(hours));
                }

                public void onFinish() {
                    if(getIntent().getStringExtra("bank_name")!=null) {
                        containerTimer.setVisibility(View.GONE);
                        containerExpiredTime.setVisibility(View.VISIBLE);
                    }
                }
            }.start();
        }

    }

    private void  finishCountdown() {
        countDownTimer.cancel();
        countDownTimer.onFinish();
        nextBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.charcoal));
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PaymentMethodTopupPresenterImpl(this, this);
    }



    @Override
    protected void onBackBtnPressed() {
        if (isFromHome) {
            onBackPressed();
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
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
    public void onSuccessRequest(TopupResponse response, String avatar, String mobile) {
        if (response.status == Integer.parseInt(Constant.TOPUP_STATUS_SUCCESS)) {
            topupResponse.setSuccess(true);
        }
        Intent intent = new Intent(TransferTopupActivity.this, StatusTopupActivity.class);
        topupResponse.setTopupTransaction(true);
        topupResponse.setCustomer_name(response.customer_name);
        topupResponse.setCustomer_avatar(avatar);
        topupResponse.setCustomer_no(mobile);
//        topupResponse.setStatus(String.valueOf(response.status));
        topupResponse.setBankName(response.account.bank.name);
        intent.putExtra(TOPUP_RESPONSE, Parcels.wrap(topupResponse));
        startActivity(intent);
    }

    @Override
    public void onSuccessRequestBanks(List<GBanks> listBank) {

    }
}
