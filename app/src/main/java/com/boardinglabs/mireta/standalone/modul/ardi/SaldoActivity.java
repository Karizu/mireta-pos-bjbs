package com.boardinglabs.mireta.standalone.modul.ardi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.entities.Trx.Detail;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.component.util.Utils;
import com.boardinglabs.mireta.standalone.modul.home.HomeActivity;
import com.cloudpos.DeviceException;
import com.cloudpos.POSTerminal;
import com.cloudpos.printer.Format;
import com.cloudpos.printer.PrinterDevice;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaldoActivity extends BaseActivity {

    private String sisaSaldo, member_name, member_lulusan, member_angkatan, no_va, str, date, time, manufacturer, model;
    private PrinterDevice printerDevice;
    private Format format;

    private Handler handler = new Handler();

    private Runnable myRunnable = () -> {
//            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
    };

    @BindView(R.id.tvSaldo)
    TextView tvSaldo;
    @BindView(R.id.memberName)
    TextView memberName;
    @BindView(R.id.memberLulusan)
    TextView memberLulusan;
    @BindView(R.id.memberAngkatan)
    TextView memberAngkatan;

    @OnClick(R.id.btnSelesai)
    void onClickSelesai(){
        Intent intent = new Intent(SaldoActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.btnPrint)
    void onClickbtnPrint(){
        if (manufacturer.equals("wizarPOS")) {
            closePrinter();
            printStruk();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_saldo;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setContentViewOnChild() {
        ButterKnife.bind(this);
        setToolbarTitle("SALDO");

        Intent intent = getIntent();
        sisaSaldo = intent.getStringExtra("sisaSaldo");
        no_va = intent.getStringExtra("no_va");
        member_name = intent.getStringExtra("member_name");
        member_lulusan = intent.getStringExtra("member_lulusan");
        member_angkatan = intent.getStringExtra("member_angkatan");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        date = Utils.dateStringFormat(dateTime, "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy");
        time = Utils.dateStringFormat(dateTime, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        int mSisaSaldo = Integer.parseInt(sisaSaldo);

        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;

        printerDevice = (PrinterDevice) POSTerminal.getInstance(getApplicationContext()).getDevice(
                "cloudpos.device.printer");

        tvSaldo.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mSisaSaldo));
        memberName.setText("Nama: "+member_name);
        memberLulusan.setText("No VA: "+no_va);
        memberAngkatan.setText("Angkatan "+member_angkatan);
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

    public void printStruk() {
        try {
            str = context.getString(R.string.openingPrint) + "\n";
            handler.post(myRunnable);
            printerDevice.open();
            str += context.getString(R.string.printerOpenSuc) + "\n";
            handler.post(myRunnable);
            format = new Format();
            try {
                if (printerDevice.queryStatus() == printerDevice.STATUS_OUT_OF_PAPER) {
                    str += context.getString(R.string.queryStatus) + "\n";
                    handler.post(myRunnable);
                    Toast.makeText(context, "The printer is short of paper", Toast.LENGTH_SHORT).show();
                } else if (printerDevice.queryStatus() == printerDevice.STATUS_PAPER_EXIST) {
                    str += context.getString(R.string.statusNor) + "\n";
                    handler.post(myRunnable);
                    Thread thread = new Thread(() -> {
                        // TODO Auto-generated method stub
                        try {

                            format.setParameter("align", "center");
                            format.setParameter("bold", "true");
                            format.setParameter("size", "medium");

                            try {
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_bjbs);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                printerDevice.printBitmap(format, bitmap);
                                printerDevice.printText("\n \n");

                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            printerDevice.printText(format, loginBusiness.name.toUpperCase() + "\n" +
                                    loginBusiness.address + "\n");
                            printerDevice.printText(format, loginStockLocation.telp != null ? loginStockLocation.telp + "\n": "-" + "\n");
                            format.clear();
                            format.setParameter("align", "center");
                            format.setParameter("size", "medium");
                            printerDevice.printText(format, "--------------------------------\n");
                            printerDevice.printText(format, date + "                " + time + "\n");
                            format.clear();
                            format.setParameter("align", "left");
                            format.setParameter("size", "medium");
                            printerDevice.printText(format, "--------------------------------\n");
                            try {
                                printerDevice.printlnText(format, "Nama  : "+member_name);
                                printerDevice.printlnText(format, "No VA : "+no_va);
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            try {
                                int mSisaSaldo = Integer.parseInt(sisaSaldo);
                                format.setParameter("align", "left");
                                format.setParameter("size", "medium");
                                printerDevice.printText(format, "Sisa Saldo :" + "\n");
                                format.clear();
                                format.setParameter("align", "right");
                                format.setParameter("size", "medium");
                                printerDevice.printText(format, "Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mSisaSaldo) + "\n");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            printerDevice.printText(format, "--------------------------------\n\n");

                            format.clear();
                            format.setParameter("align", "center");
                            format.setParameter("size", "medium");
                            printerDevice.printlnText(format, "Terimakasih sudah berbelanja");
                            try {
                                printerDevice.printlnText(format, member_name);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            printerDevice.printlnText(format, "\n");
                            printerDevice.printlnText(format, "\n");

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                } else {
                    Toast.makeText(context, "The printer is short of paper", Toast.LENGTH_SHORT).show();
                }
            } catch (DeviceException de) {
                str += context.getString(R.string.checkStatus) + "\n";
                handler.post(myRunnable);
                de.printStackTrace();
            }
        } catch (DeviceException de) {
            de.printStackTrace();
            str += context.getString(R.string.openFailed) + "\n";
            handler.post(myRunnable);
        }
    }

    private void closePrinter() {
        try {
            printerDevice.close();
            str += context.getString(R.string.closeSuc) + "\n";
            handler.post(myRunnable);
        } catch (DeviceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str += context.getString(R.string.closeFailed) + "\n";
            handler.post(myRunnable);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
