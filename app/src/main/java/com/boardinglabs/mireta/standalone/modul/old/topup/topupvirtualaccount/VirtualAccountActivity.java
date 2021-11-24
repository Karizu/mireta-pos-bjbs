package com.boardinglabs.mireta.standalone.modul.old.topup.topupvirtualaccount;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dhimas on 2/25/18.
 */

public class VirtualAccountActivity extends BaseActivity {
    public static final String EXPIRED_DATE = "expiredDate";
    public static final String AMOUNT_VA = "amountVA";
    public static final String IS_FROM_HISTORY = "isFromHistory";
    private TransactionTopupResponse topupResponse;
    private TextView accountName;
    private TextView accountNumber;
    private TextView totalPayment;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private LinearLayout containerTimer;
    private LinearLayout containerExpiredTime;
    private CountDownTimer countDownTimer;
    private boolean isBackToPrevious;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.virtual_account_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Virtual Account");
//        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
        String[] user = PreferenceManager.getUserInfo();
        String mobile = user[1];
        String accounNo = "7080" + mobile.substring(1);
        String totalAmount = getIntent().getStringExtra(AMOUNT_VA);
        totalPayment = (TextView) findViewById(R.id.total_payment);
        accountNumber = (TextView) findViewById(R.id.account_no);
        TextView time = (TextView) findViewById(R.id.time);
        TextView dateTextView = (TextView) findViewById(R.id.date);
        hour = (TextView) findViewById(R.id.jam);
        minute = (TextView) findViewById(R.id.menit);
        second = (TextView) findViewById(R.id.detik);
        containerTimer = (LinearLayout) findViewById(R.id.container_countdown);
        containerExpiredTime = (LinearLayout) findViewById(R.id.note_expired_time);
        String expiredDate = getIntent().getStringExtra(EXPIRED_DATE);
        isBackToPrevious = getIntent().getBooleanExtra(IS_FROM_HISTORY, false);
        totalPayment.setText("Rp " + MethodUtil.toCurrencyFormat(totalAmount));
        accountNumber.setText(accounNo);

        try {
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id")).parse(expiredDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id","ID"));
            dateTextView.setText(dateFormat.format(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        Date endDate = null;
        final long endTime = System.currentTimeMillis();
        try {
            endDate = formatter.parse(expiredDate);
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
                    containerTimer.setVisibility(View.GONE);
                    containerExpiredTime.setVisibility(View.VISIBLE);
                }
            }.start();
        }
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        if (isBackToPrevious) {
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

    private void finishCountdown() {
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }
}
