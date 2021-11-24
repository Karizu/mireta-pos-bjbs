package com.boardinglabs.mireta.standalone.modul.old.scanqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.modul.old.transactionreview.TransactionReviewActivity;

/**
 * Created by Dhimas on 2/1/18.
 */

public class InputAmountActivity extends AppCompatActivity {
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

    private Button confirmButton;
//    private EditText detail;
    private EditText total;
    private ImageView buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_new_order);
        initComponent();
        initEvent();
    }

    private void initComponent(){
        confirmButton = (Button) findViewById(R.id.confirm_button);
//        detail = (EditText) findViewById(R.id.detail_id);
        total = (EditText) findViewById(R.id.total_id);
        buttonBack = (ImageView) findViewById(R.id.button_close_order);
    }

    private void initEvent(){
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("kunam", total.getText().toString());
                Intent intent = new Intent(getApplication(), TransactionReviewActivity.class);
                intent.putExtra(TransactionReviewActivity.TOTAL_TRANSACTION, total.getText().toString());
                intent.putExtra(TransactionReviewActivity.ORDER_ID, getIntent().getStringExtra(TransactionReviewActivity.ORDER_ID));
                intent.putExtra(TransactionReviewActivity.IS_FROM_QR, true);
                intent.putExtra(TransactionReviewActivity.TRANSACTION_INFO, getIntent().getStringExtra(TransactionReviewActivity.TRANSACTION_INFO));
                intent.putExtra(TransactionReviewActivity.IMAGE_DRAWABLE, R.drawable.ic_merchant);
                startActivity(intent);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
