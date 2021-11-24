package com.boardinglabs.mireta.standalone.modul.old.topup.topupstatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionTopupResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;

import org.parceler.Parcels;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.topup.topuptransfer.TransferTopupActivity.TOPUP_RESPONSE;

/**
 * Created by Dhimas on 9/29/17.
 */

public class StatusTopupActivity extends AppCompatActivity {
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
    private Context c;

    //Account Info
    private LinearLayout accountContainer;
    private TextView accountTitle;
    private TextView accountName;
    private TextView accountInfo;
    private ImageView accountImage;

    private TextView statusText;
    private RelativeLayout statusTextWrapper;

    private static String telkomselRegex = "^(\\+62|\\+0|0|62)8(1[123]|52|53|21|22|23)[0-9]{5,9}$";
    private static String simpatiRegex = "^(\\+62|\\+0|0|62)8(1[123]|2[12])[0-9]{5,9}$";
    private static String asRegex = "^(\\+62|\\+0|0|62)8(52|53|23)[0-9]{5,9}$";
    private static String triRegex = "^(\\+62|\\+0|0|62)8(96|97|98|99|95)[0-9]{5,9}$";
    private static String smartfrenRegex = "^(\\+62|\\+0|0|62)8(81|82|83|84|85|86|87|88|89)[0-9]{5,9}$";
    private static String axisRegex = "^(\\+62|\\+0|0|62)8(38|31|32|33)[0-9]{5,9}$";
    private static String indosatRegex = "^(\\+62815|0815|62815|\\+0815|\\+62816|0816|62816|\\+0816|\\+62858|0858|62858|\\+0814|\\+62814|0814|62814|\\+0814)[0-9]{5,9}$";
    private static String im3Regex = "^(\\+62855|0855|62855|\\+0855|\\+62856|0856|62856|\\+0856|\\+62857|0857|62857|\\+0857)[0-9]{5,9}$";
    private static String xlRegex = "^(\\+62817|0817|62817|\\+0817|\\+62818|0818|62818|\\+0818|\\+62819|0819|62819|\\+0819|\\+62859|0859|62859|\\+0859|\\+0878|\\+62878|0878|62878|\\+0877|\\+62877|0877|62877)[0-9]{5,9}$";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_topup_activity);
        topupResponse = Parcels.unwrap(getIntent().getParcelableExtra(TOPUP_RESPONSE));
        c = this;
        initComponent();
        initEvent();
    }

    private void initComponent() {
        accountContainer = (LinearLayout) findViewById(R.id.account_container);
        accountTitle = (TextView) findViewById(R.id.account_title);
        accountName = (TextView) findViewById(R.id.account_name);
        accountInfo = (TextView) findViewById(R.id.account_info);
        accountImage = (ImageView) findViewById(R.id.account_image);

        statusText = (TextView) findViewById(R.id.success_status);
        statusTextWrapper = (RelativeLayout) findViewById(R.id.success_status_wrapper);

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
//                    finish();
                    Intent intent = new Intent(StatusTopupActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StatusTopupActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });

//        if(topupResponse.getTopup()!=null){
//            GTransactionTopup topup = topupResponse.getTopup();
//            if(topup.sub_customer!=null){
//                long temp = Long.parseLong(topup.balance_before) - Long.parseLong(topup.balance_after);
//                if (temp > 0) {
//                    accountTitle.setText("PENERIMA");
//                }else{
//                    accountTitle.setText("PENGIRIM");
//                }
//                accountName.setText(topup.sub_customer.name);
//                accountInfo.setText(topup.sub_customer.mobile);
//            }else{
//                accountTitle.setText("AKUN");
//            }
//        }

        //Log.i("kunam1", topupResponse.getStatus());

        if(topupResponse.getStatusTrx().equalsIgnoreCase("7") || topupResponse.getStatus().equalsIgnoreCase("4")){
            statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pending_circle));
            statusTextWrapper.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_status_pending));
            statusText.setText("MENUNGGU");
            statusText.setTextColor(getResources().getColor(R.color.pending));
        }
        if ((topupResponse.getStatus() != null && topupResponse.getStatus().equalsIgnoreCase(Constant.TOPUP_STATUS_REJECT)) ||
                topupResponse.isFail()) {
            statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fail_circle));
            statusInfo.setText("Transaksi pembayaran tidak ditemukan");
            greetingStatus.setText("DITOLAK");
            statusText.setText("GAGAL");
            statusText.setTextColor(getResources().getColor(R.color.red));
            statusTextWrapper.setBackgroundResource(R.drawable.border_round_red);
        } else {
            if (topupResponse.isSuccess()) {
                greetingStatus.setText("TERIMA KASIH");
                statusInfo.setText("Transaksi anda telah berhasil");


            } else {
//                statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_status_pending));
                statusLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pending_circle));
                statusInfo.setText("Sistem kami sedang melakukan verifikasi\n" +
                        "mungkin membutuhkan waktu");
                greetingStatus.setText("DALAM PROSES");
                statusText.setText("PROSES");
                statusText.setTextColor(getResources().getColor(R.color.pending));
                statusTextWrapper.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_status_pending));
            }
        }

        if (topupResponse.isFromHome()) {
            isFromHome = true;
        }
        transactionInfo.setText(topupResponse.getInfo());
        time.setText(topupResponse.getTime());
        date.setText(topupResponse.getDate());
        orderId.setText(topupResponse.getOrderId());
        bankName.setText(topupResponse.getBankName());
        totalAmount.setText(MethodUtil.toCurrencyFormat(topupResponse.getTopupSaldo()));
        if (!TextUtils.isEmpty(topupResponse.getNotes())) {
            noteContainer.setVisibility(View.VISIBLE);
            notePayment.setText(topupResponse.getNotes());
        }

//        Log.i("kunam", String.valueOf(topupResponse.getJenisTransaksi()));
//        Log.i("kunam", topupResponse.getCustomer_no());
//        Log.i("kunam", topupResponse.getService_category());

        if(topupResponse.getJenisTransaksi()==1){
            if(topupResponse.getSub_customer_name()==null) {
                accountName.setText(topupResponse.getCustomer_name());
                accountInfo.setText(topupResponse.getSub_customer_no());
                if(topupResponse.getCustomer_avatar()!=null){
                    setRoundedImage(topupResponse.getCustomer_avatar());
                }
                statusInfo.setText("Topup" + (topupResponse.isSuccess()?" Berhasil!":" Gagal"));
            }else{
                long temp = Long.parseLong(topupResponse.balance_before) - Long.parseLong(topupResponse.balance_after);
                if (temp > 0) {
                    accountTitle.setText("PENERIMA");
                    statusInfo.setText("Transfer" + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
                }else{
                    accountTitle.setText("PENGIRIM");
                    statusInfo.setText("Request" + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
                }
                accountName.setText(topupResponse.getSub_customer_name());
                if(topupResponse.getSub_customer_no()!=null) {
                    accountInfo.setText(topupResponse.getSub_customer_no());
                }else{
                    accountInfo.setText(topupResponse.getCustomer_no());
                }
                if(topupResponse.getSub_customer_avatar()!=null) {
                    setRoundedImage(topupResponse.getSub_customer_avatar());
                }
            }
            if(topupResponse.getBankName()==null) {
                bankName.setText("doompet");
                bankImg.setImageResource(R.drawable.ic_doompet);
            }else{
                bankName.setText("BANK TRANSFER");
                switch(topupResponse.getBankName()){
                    case "Bank Mandiri" : Glide.with(c).load(R.drawable.ic_bank_mandiri).dontAnimate().into(bankImg);break;
                    case "Bank BCA" : Glide.with(c).load(R.drawable.ic_bank_bca).dontAnimate().into(bankImg);break;
                    case "Bank Permata" : Glide.with(c).load(R.drawable.ic_bank_permata).dontAnimate().into(bankImg);break;
                    case "Bank BNI" : Glide.with(c).load(R.drawable.ic_bank_bni).dontAnimate().into(bankImg);break;
                    case "Bank UOB" : Glide.with(c).load(R.drawable.ic_bank_uob).dontAnimate().into(bankImg);break;
                    case "Bank Woori Saudara" : Glide.with(c).load(R.drawable.ic_bank_woori_saudara).dontAnimate().into(bankImg);break;
                    case "Bank Cimb Niaga" : Glide.with(c).load(R.drawable.ic_bank_cimb_niaga).dontAnimate().into(bankImg);break;
                    default : Glide.with(c).load(R.drawable.pampasy_logo).dontAnimate().into(bankImg);break;
                }
            }
        }else if(topupResponse.getJenisTransaksi()==2){
            accountTitle.setText("PENJUAL");
            accountName.setText(topupResponse.getMerchant_name());
            accountInfo.setVisibility(View.INVISIBLE);
            accountImage.setVisibility(View.INVISIBLE);
            bankName.setText("doompet");
            bankImg.setImageResource(R.drawable.ic_doompet);
            statusInfo.setText("Pembayaran Merchant" + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
        }else{
            accountTitle.setText("AKUN");
            accountName.setText(((topupResponse.getCustomer_name()!=null&&!topupResponse.getCustomer_name().isEmpty())?(topupResponse.getCustomer_name()+"\n"):"") + topupResponse.getCustomer_no());
            accountInfo.setText(topupResponse.getService_name());
            String statusText = "";
            if(topupResponse.getService_category()!=null) {
                switch (topupResponse.getService_category()) {
                    case Constant.SERVICE_PULSA: {
                        statusText = "Pembelian Pulsa";
                        if (topupResponse.getCustomer_no().matches(xlRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_xl));
                        } else if (topupResponse.getCustomer_no().matches(indosatRegex) || topupResponse.getCustomer_no().matches(im3Regex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_indosat));
                        } else if (topupResponse.getCustomer_no().matches(telkomselRegex) || topupResponse.getCustomer_no().matches(simpatiRegex) || topupResponse.getCustomer_no().matches(asRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_telkomsel));
                        } else if (topupResponse.getCustomer_no().matches(triRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_tri));
                        } else if (topupResponse.getCustomer_no().matches(smartfrenRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.smartfren));
                        } else if (topupResponse.getCustomer_no().matches(axisRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.axis));
                        } else {
                            accountImage.setImageResource(R.drawable.ic_phone);
                        }
                    }
                    break;
                    case Constant.SERVICE_PAKET_DATA:
                        statusText = "Pembelian Data";
                        if (topupResponse.getCustomer_no().matches(xlRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_xl));
                        } else if (topupResponse.getCustomer_no().matches(indosatRegex) || topupResponse.getCustomer_no().matches(im3Regex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_indosat));
                        } else if (topupResponse.getCustomer_no().matches(telkomselRegex) || topupResponse.getCustomer_no().matches(simpatiRegex) || topupResponse.getCustomer_no().matches(asRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_telkomsel));
                        } else if (topupResponse.getCustomer_no().matches(triRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_tri));
                        } else if (topupResponse.getCustomer_no().matches(smartfrenRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.smartfren));
                        } else if (topupResponse.getCustomer_no().matches(axisRegex)) {
                            accountImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.axis));
                        } else {
                            accountImage.setImageResource(R.drawable.ic_data);
                        }
                        break;
                    case Constant.SERVICE_PDAM:
                        accountImage.setImageResource(R.drawable.ic_pdam);
                        statusText = "Pembayaran PDAM";
                        break;
                    case Constant.SERVICE_PLN:
                        accountImage.setImageResource(R.drawable.ic_pln);
                        statusText = "Pembayaran PLN";
                        break;
                    case Constant.SERVICE_TELKOM:
                        accountImage.setImageResource(R.drawable.ic_data);
                        statusText = "Pembayaran Internet";
                        break;
                    case Constant.SERVICE_MULTIFINANCE:
                        accountImage.setImageResource(R.drawable.ic_cicilan);
                        statusText = "Pembayaran Cicilan";
                        break;
                    case Constant.SERVICE_ASURANSI:
                        accountImage.setImageResource(R.drawable.assetjiwasraya);
                        statusText = "Pembayaran Jiwasraya";
                        break;
                    case Constant.SERVICE_KARTU_KREDIT:
                        accountImage.setImageResource(R.drawable.ic_idr);
                        statusText = "Pembayaran Kartu Kredit";
                        break;
                    case Constant.SERVICE_ISP:
                        accountImage.setImageResource(R.drawable.ic_internet);
                        statusText = "Pembayaran Internet";
                        break;
                    case Constant.SERVICE_BPJS:
                        accountImage.setImageResource(R.drawable.ic_bpjs);
                        statusText = "Pembayaran BPJS";
                        break;
                    default:
                        accountImage.setImageResource(R.drawable.ic_check);
                        statusText = "Pembayaran";
                        break;
                }
            }
            statusInfo.setText(statusText + (topupResponse.isSuccess()?" Berhasil!":((topupResponse.isFail)?" Gagal":" Menunggu")));
            bankName.setText("doompet");
            bankImg.setImageResource(R.drawable.ic_doompet);
            if(topupResponse.getJenisTransaksi()==0 && topupResponse.getService_category()==null){
                if(topupResponse.getCustomer_avatar()!=null){
                    setRoundedImage(topupResponse.getCustomer_avatar());
                }
                bankName.setText("Bank Transfer");
                bankImg.setImageResource(R.drawable.ic_doompet);
                if(topupResponse.getBankName()!=null){
                    switch(topupResponse.getBankName()){
                        case "Bank Mandiri" : Glide.with(c).load(R.drawable.ic_bank_mandiri).dontAnimate().into(bankImg);break;
                        case "Bank BCA" : Glide.with(c).load(R.drawable.ic_bank_bca).dontAnimate().into(bankImg);break;
                        case "Bank Permata" : Glide.with(c).load(R.drawable.ic_bank_permata).dontAnimate().into(bankImg);break;
                        case "Bank BNI" : Glide.with(c).load(R.drawable.ic_bank_bni).dontAnimate().into(bankImg);break;
                        case "Bank UOB" : Glide.with(c).load(R.drawable.ic_bank_uob).dontAnimate().into(bankImg);break;
                        case "Bank Woori Saudara" : Glide.with(c).load(R.drawable.ic_bank_woori_saudara).dontAnimate().into(bankImg);break;
                        case "Bank Cimb Niaga" : Glide.with(c).load(R.drawable.ic_bank_cimb_niaga).dontAnimate().into(bankImg);break;
                        default : Glide.with(c).load(R.drawable.ic_doompet).dontAnimate().into(bankImg);break;
                    }
                }
//                if(topupResponse.getCustomer_no()!=null){
//                    accountInfo.setText(topupResponse.getCustomer_no());
//                }
            }
        }
    }

    private void setRoundedImage(String base64Image){
        byte[] imageByteArray = Base64.decode(base64Image, Base64.DEFAULT);
        Glide.with(this).load(imageByteArray).asBitmap().centerCrop().into(new BitmapImageViewTarget(accountImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(c.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                accountImage.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
