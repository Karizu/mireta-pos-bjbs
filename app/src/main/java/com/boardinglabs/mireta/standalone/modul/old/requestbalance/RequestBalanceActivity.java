package com.boardinglabs.mireta.standalone.modul.old.requestbalance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.checkpasscode.CheckPasscodeActivity;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Dhimas on 11/29/17.
 */

public class RequestBalanceActivity extends BaseActivity implements CommonInterface, RequestBalanceView {
    private EditText inputAmount;
    private Button sendBtn;
    private CircleImageView avatarImageView;
    private TextView destCustNameTV;
    private String destCustId;
    private String destCustPhoneNumber;
    private String destCustName;
    private String destCustAvatar;
    private RequestBalancePresenter mPresenter;
    public static int REQUEST_CODE_PICK_CONTACTS = 101;

    private Context mContext;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_request_balance;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("REQUEST");
        mContext = this;
        sendBtn = (Button) findViewById(R.id.send_btn);

        inputAmount = (EditText) findViewById(R.id.amount);
        destCustNameTV = (TextView) findViewById(R.id.dest_cust_name_tv);
        avatarImageView = (CircleImageView) findViewById(R.id.dest_image_contact);

        //inputAmount.setText("0");

        destCustId = getIntent().getStringExtra("dest_cust_id");
        destCustPhoneNumber = getIntent().getStringExtra("dest_cust_phone_number");
        destCustName = getIntent().getStringExtra("dest_cust_name");

        destCustAvatar = getIntent().getStringExtra("dest_cust_avatar");

        destCustNameTV.setText(destCustName);

        if (destCustAvatar != null && !destCustAvatar.equals("")){
            byte[] decodedString = Base64.decode(destCustAvatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImageView.setImageBitmap(decodedByte);
        }
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new RequestBalancePresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void initEvent() {
        RxView.clicks(sendBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!TextUtils.isEmpty(inputAmount.getText().toString()) && Double.parseDouble(inputAmount.getText().toString())>0) {
                    Intent intent = new Intent(RequestBalanceActivity.this, CheckPasscodeActivity.class);
                    String[] user = PreferenceManager.getUserInfo();
                    GAgent agent = PreferenceManager.getAgent();
                    String phone = "0";
                    if (user != null && user[1] != null) {
                        phone = user[1];
                    } else if (agent != null && agent.mobile != null) {
                        phone = agent.mobile;
                    }
                    intent.putExtra(OtpActivity.MOBILE, phone);
                    intent.putExtra("hinttext", "Ketik kode PIN untuk validasi transaksi.");
                    startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(mContext,"Anda harus menginputkan nominal",Toast.LENGTH_LONG).show();
                }
            }
        });
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
    public void onSuccessRequestBalance() {
        String isBack = getIntent().getStringExtra("is_back");
        if (isBack != null){
            MethodUtil.showCustomToast(this, "Request Transfer Sukses", R.drawable.ic_check_circle);
            onBackBtnPressed();
        }
        else {
            MethodUtil.showCustomToast(this, "Request Transfer Sukses", R.drawable.ic_check_circle);
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.requestBalance(destCustId, inputAmount.getText().toString(), "");
        }

    }

}
