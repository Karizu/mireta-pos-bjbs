package com.boardinglabs.mireta.standalone.modul.old.invite;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyContactCustomerInviteAdapter;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GContact;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends BaseActivity implements InviteView, ListActionListener,  CommonInterface{
    private static final String TAG = "INVITE FRIENDS" ;
    private RecyclerView listView;
    private RecyContactCustomerInviteAdapter listAdapter;
    private List<GContact> contactList;
    private TextView invitationCode;
    private Button inviteButton;

    private InvitePresenter mPresenter;
    protected static CustomProgressBar progressBar = new CustomProgressBar();

    private Context mContext;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("UNDANG TEMAN");
    }


    @Override
    protected void onCreateAtChild() {
        mPresenter = new InvitePresenterImpl(this, this);
        initData();
    }

    private void initData() {
        mContext = this;

        inviteButton = (Button) findViewById(R.id.inviteButton);
        inviteButton.setVisibility(View.GONE);

        listAdapter = new RecyContactCustomerInviteAdapter(new RecyContactCustomerInviteAdapter.InviteListListener() {
            @Override
            public void onClickInvite(int position, boolean isChecked) {
                contactList.get(position).setSelected(isChecked);
                int checkedCounter = 0;
                for(GContact c : contactList){
                    if(c.isSelected()){
                        checkedCounter++;
                    }
                }
                inviteButton.setVisibility(checkedCounter>0?View.VISIBLE:View.GONE);
                listAdapter.notifyDataSetChanged();
            }
        });

        listView = (RecyclerView) findViewById(R.id.contact_list);
        listView.setLayoutManager(new LinearLayoutManager(this));

        invitationCode = (TextView) findViewById(R.id.invitationCode);
        GAgent user = PreferenceManager.getAgent();
        invitationCode.setText(user.username);
        invitationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Referal Code", invitationCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext,invitationCode.getText().toString() + " ditambahkan ke clipboard", Toast.LENGTH_LONG ).show();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contacs = "";
                for(GContact g : contactList){
                    if(g.isSelected()){
                        contacs = contacs + g.getContact() + ",";
                    }
                }
                if(!contacs.isEmpty()) {
//                    Toast.makeText(context, contacs.substring(0, contacs.length() - 1), Toast.LENGTH_LONG).show();
                    mPresenter.sentInvite(contacs);
                }else{
                    Toast.makeText(context,"Pilih minimal satu kontak untuk melakukan invite", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setAdapter(listAdapter);
        contactList = new ArrayList<>();
        getContactList();

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void getContactList() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
            this.contactList = new ArrayList<>();
            while (phones.moveToNext())
            {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
                String countryCode = MethodUtil.GetCountryZipCode(this);
                phoneNumber = phoneNumber.replace("+"+countryCode, "0");
                if (phoneNumber.substring(0,1).equals("0") && phoneNumber.length()>6){
                    this.contactList.add(new GContact(name,phoneNumber));
                }

            }
            phones.close();
            listAdapter.setDataContact(contactList);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContactList();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void itemClicked(int position) {

    }

    @Override
    public void itemDeleted(int position) {

    }

    @Override
    public void onSuccessInvite(String message) {
        Toast.makeText(context,"Sukses Melakukan invite",Toast.LENGTH_LONG).show();
        onBackBtnPressed();
    }
}