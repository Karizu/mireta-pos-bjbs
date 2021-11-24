package com.boardinglabs.mireta.standalone.modul.old.scanqr;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CostumInputDialogFragment;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.bayar.BayarActivity;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;
import com.boardinglabs.mireta.standalone.modul.old.transactionreview.TransactionReviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/1/18.
 */

public class ScanQRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener, CommonInterface, ScanQRView, CostumInputDialogFragment.InputListener {
    private QRCodeReaderView mCodeReaderView;
    public static final String RESULT_READ_QRCODE = "qrCodeResult";
    public static final String IS_TRANSACTION = "isTransaction";
    public static final int DIALOG_SCAN_QRCODE = 230;
    private ScanQRPresenter mPresenter;
    private ImageView menu;
    private boolean isAlreadyHit;
    private LinearLayout bayar;
    private Button inputCode;
    private ImageView backButton;
    private boolean isAlreadyClick = false;
    private static CustomProgressBar progressBar = new CustomProgressBar();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_layout_activity);
        mPresenter = new ScanQRPresenterImpl(getService(), this, this);
        mCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mCodeReaderView.setOnQRCodeReadListener(this);
        menu = (ImageView) findViewById(R.id.menu);
        bayar = (LinearLayout) findViewById(R.id.bayar);
        inputCode = (Button) findViewById(R.id.input_code);

        backButton = (ImageView) findViewById(R.id.backbutton);

        RxView.clicks(menu).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(ScanQRActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        RxView.clicks(backButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackPressed();
            }
        });

        RxView.clicks(inputCode).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showInputDialog();
            }
        });

        RxView.clicks(bayar).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(ScanQRActivity.this, BayarActivity.class));
            }
        });
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mCodeReaderView.stopCamera();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_READ_QRCODE, text);
        try {
            JSONObject obj = new JSONObject(text);
            Object merchantId = obj.get("merchant_id");
            if (!isAlreadyHit) {
                mPresenter.scanCode(obj.get("code").toString());
                isAlreadyHit = true;
            }
            if (merchantId != null) {
                resultIntent.putExtra(IS_TRANSACTION, true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MethodUtil.showCustomToast(this, "Kode QR Tidak dikenali", R.drawable.ic_error_login);
            isAlreadyHit = false;
            isAlreadyClick = false;
            mCodeReaderView.startCamera();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        isAlreadyHit = false;
        isAlreadyClick = false;
        mCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeReaderView.stopCamera();
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
            PreferenceManager.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        isAlreadyHit = false;
        isAlreadyClick = false;
    }

    @Override
    public void openTransactionActivity(String merchantId, String amount, String notes, String orderId,
                                        String merchantName, String voucherId, String type) {
        QuickResponse qr = new QuickResponse();
        qr.setAmount(amount);
        qr.setMerchant_id(merchantId);
        qr.setNotes(notes);
        qr.setOrderId(orderId);
        qr.setMerchantName(merchantName);
        qr.setVoucherId(voucherId);
        PreferenceManager.setQrResponse(qr);
        if(type.equalsIgnoreCase("1")){
            Log.i("kunam", orderId);
            Intent intent = new Intent(this, InputAmountActivity.class);
            intent.putExtra(TransactionReviewActivity.ORDER_ID, orderId);
            intent.putExtra(TransactionReviewActivity.IS_FROM_QR, true);
            intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO, notes);
            intent.putExtra(TransactionReviewActivity.IMAGE_DRAWABLE, R.drawable.ic_merchant);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, TransactionReviewActivity.class);
            intent.putExtra(TransactionReviewActivity.TOTAL_TRANSACTION, amount);
            intent.putExtra(TransactionReviewActivity.ORDER_ID, orderId);
            intent.putExtra(TransactionReviewActivity.IS_FROM_QR, true);
            intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO, notes);
            intent.putExtra(TransactionReviewActivity.IMAGE_DRAWABLE, R.drawable.ic_merchant);
            startActivity(intent);
        }
//        intent.putExtra(Constant.QR_RESULT, Parcels.wrap(qr));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showInputDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(ScanQRActivity.class.getName());
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                CostumInputDialogFragment dialogFragment = CostumInputDialogFragment.getInstance("","", true, true);
                dialogFragment.setListener(ScanQRActivity.this);
                dialogFragment.setCancelable(true);
                dialogFragment.show(ft, ScanQRActivity.class.getName());
            }
        });
    }

    @Override
    public void onClickNextBtn(String input) {
        if (!isAlreadyClick) {
            mPresenter.scanCode(input);
            isAlreadyClick = true;
        }
    }
}
