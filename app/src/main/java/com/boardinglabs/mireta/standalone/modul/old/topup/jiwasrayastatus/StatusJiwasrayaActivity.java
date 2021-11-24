package com.boardinglabs.mireta.standalone.modul.old.topup.jiwasrayastatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity.TOPUP_RESPONSE;

/**
 * Created by Dhimas on 9/29/17.
 */

public class StatusJiwasrayaActivity extends AppCompatActivity {
    private ImageView backButton;
    private TextView greetingStatus;
    private TextView statusInfo;
    private TextView date;
    private TextView time;
    private TextView orderId;
    private TextView transactionInfo;
    private TextView bankName;
    private TextView notePayment;
    private ImageView bankImg;
    private TextView totalAmount;
    private ImageView statusLogo;
    private LinearLayout paymentMethod;
    private LinearLayout noteContainer;
    private TransactionTopupResponse topupResponse;
    private boolean isFromHome;

    private String status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_jiwasraya_payment_activity);
//        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
//        status = getIntent().getStringExtra("statusJiwasraya");
        String transactionData=getIntent().getStringExtra(TOPUP_RESPONSE);
        String transactionId=getIntent().getStringExtra("transactionId");
        String transactionBillName=getIntent().getStringExtra("billName");
        String transactionCustNo="";
        String transactionCustName="";
        String transactionTotal=getIntent().getStringExtra("totalAmount");
        transactionCustNo=getIntent().getStringExtra("customerNo");
        transactionCustName=getIntent().getStringExtra("customeName");
        try {
            JSONObject jsonData = new JSONObject(transactionData);
            status = jsonData.get("statusDescription").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initComponent();
        initEvent();

        String dateTime=getIntent().getStringExtra("updatedAt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
        SimpleDateFormat fmtOut2 = new SimpleDateFormat("dd, MMMM yyyy");
//        fmtOut.format(date);
        time.setText(fmtOut.format(date).toString());
        this.date.setText(fmtOut2.format(date).toString());
        orderId.setText(transactionId);
        transactionInfo.setText(transactionBillName+" "+transactionCustNo+" atas nama "+transactionCustName);
        totalAmount.setText(MethodUtil.toCurrencyFormat(transactionTotal));
    }

    private void initComponent() {
        backButton = (ImageView) findViewById(R.id.backButton);
        greetingStatus = (TextView) findViewById(R.id.greeting_status);
        statusInfo = (TextView) findViewById(R.id.status_info);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        orderId = (TextView) findViewById(R.id.order_id_text);
        transactionInfo = (TextView) findViewById(R.id.transaction_info);
        bankName = (TextView) findViewById(R.id.bank_name);
        bankImg = (ImageView) findViewById(R.id.bank_img);
        statusLogo = (ImageView) findViewById(R.id.status_logo);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        paymentMethod = (LinearLayout) findViewById(R.id.payment_method);
        noteContainer = (LinearLayout) findViewById(R.id.payment_note_container);
        notePayment = (TextView) findViewById(R.id.notes_transfer);
    }

    private void initEvent() {
        RxView.clicks(backButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isFromHome) {
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });



//        if ((topupResponse.getStatus() != null && topupResponse.getStatus().equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) ||
//                topupResponse.isFail()) {
//            statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.reject));
//            statusInfo.setText("Transaksi pembayaran tidak ditemukan");
//            greetingStatus.setText("DITOLAK");
//        } else {

//            if (status.equalsIgnoreCase("sukses")) {
                greetingStatus.setText("TERIMA KASIH");
                statusInfo.setText("Transaksi anda telah berhasil");
//            } else {
//                statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.process));
//                statusInfo.setText("Sistem kami sedang melakukan verifikasi\n" +
//                        "mungkin membutuhkan waktu");
//                greetingStatus.setText("DALAM PROSES");
//            }
//        }

//        if (!topupResponse.isTopupTransaction()) {
//            paymentMethod.setVisibility(View.GONE);
//        }
//
//        if (topupResponse.isFromHome()) {
//            isFromHome = true;
//        }
//        transactionInfo.setText(topupResponse.getInfo());
//        time.setText(topupResponse.getTime());
//        date.setText(topupResponse.getDate());
//        orderId.setText(topupResponse.getOrderId());
//        bankName.setText(topupResponse.getBankName());
//        totalAmount.setText(MethodUtil.toCurrencyFormat(topupResponse.getTopupSaldo()));
//        if (!TextUtils.isEmpty(topupResponse.getNotes())) {
//            noteContainer.setVisibility(View.VISIBLE);
//            notePayment.setText(topupResponse.getNotes());
//        }
    }
}
