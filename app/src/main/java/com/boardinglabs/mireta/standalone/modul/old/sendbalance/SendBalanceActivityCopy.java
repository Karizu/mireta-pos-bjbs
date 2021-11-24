package com.boardinglabs.mireta.standalone.modul.old.sendbalance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

import rx.functions.Action1;

/**
 * Created by Dhimas on 11/29/17.
 */

public class SendBalanceActivityCopy extends BaseActivity implements CommonInterface, SendBalanceView {
    private EditText inputMobile;
    private EditText inputTransfer;
    private EditText inputNotes;
    private Button sendBtn;
    private SendBalancePresenter mPresenter;
    private ImageView phoneBook;
    public static int REQUEST_CODE_PICK_CONTACTS = 101;
    private Uri uriContact;
    private String contactID;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.send_balance_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("TRANSFER");
        inputMobile = (EditText) findViewById(R.id.sender_mobile);
        inputTransfer = (EditText) findViewById(R.id.amount_transfer);
        inputNotes = (EditText) findViewById(R.id.notes_transfer);
        sendBtn = (Button) findViewById(R.id.send_btn);
        phoneBook = (ImageView) findViewById(R.id.phone_book);
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new SendBalancePresenterImpl(this, this);
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
                if (!TextUtils.isEmpty(inputMobile.getText().toString()) &&
                        !TextUtils.isEmpty(inputTransfer.getText().toString())) {
                    Intent intent = new Intent(SendBalanceActivityCopy.this, CheckPasscodeActivity.class);
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
        });

        RxView.clicks(phoneBook).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ActivityCompat.requestPermissions(SendBalanceActivityCopy.this, new String[]{Manifest.permission.READ_CONTACTS}, 10);
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
//        Log.i("kunamkunaman", msg);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    public void onSuccessSendBalance() {
        MethodUtil.showCustomToast(this, "Transfer sukses", R.drawable.ic_check_circle);
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
        if (requestCode == REQUEST_CODE_PICK_CONTACTS) {
            if (resultCode == RESULT_OK) {
                uriContact = data.getData();
                retrieveContactNumber();
            }
        } else {
            if (resultCode == RESULT_OK) {
                mPresenter.sendBalance(inputMobile.getText().toString(), inputTransfer.getText().toString(), inputNotes.getText().toString());
            }
        }

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

//        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactNumber = contactNumber.replace("+62", "0").replace("-","").replace(" ","");
            inputMobile.setText(contactNumber);
        }

        cursorPhone.close();

//        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }
}
