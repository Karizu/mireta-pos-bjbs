package com.boardinglabs.mireta.standalone.modul.old.customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyContactCustomerAdapter;
import com.boardinglabs.mireta.standalone.component.adapter.RecyclerItemClickListener;
import com.boardinglabs.mireta.standalone.component.dialog.CustomProgressBar;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.requestbalance.RequestBalanceActivity;
import com.boardinglabs.mireta.standalone.modul.old.sendbalance.SendBalanceActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactCustomerActivity extends BaseActivity implements ListActionListener, ContactCustomerPresenter.SuccessSync, CommonInterface {
    private static final String TAG = "CUSTOMER CONTACT" ;
    private RecyclerView listView;
    private EditText contactSearchBar;
    private RecyContactCustomerAdapter listAdapter;
    private String screen_type;
    private List<String> contactList;
    private List<GCostumer> customerData;
    private ContactCustomerPresenter mPresenter;
    protected static CustomProgressBar progressBar = new CustomProgressBar();

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.contact_customer_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        String title = "TRANSFER";
        screen_type = getIntent().getStringExtra("screen_type");
        if (screen_type != null && screen_type.equalsIgnoreCase("request")){
            title = "REQUEST";
        }
        setToolbarTitle(title);
    }


    @Override
    protected void onCreateAtChild() {
        initData();
    }

    private void initData() {
        listAdapter = new RecyContactCustomerAdapter(this, new ArrayList<GCostumer>());

        listView = (RecyclerView) findViewById(R.id.contact_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(listAdapter);
        listView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, listView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (screen_type.equals("transfer")){
                            Intent intent = new Intent(ContactCustomerActivity.this, SendBalanceActivity.class);
                            GCostumer selectedContact = listAdapter.getItemAtPosition(position);
                            intent.putExtra("dest_cust_id", selectedContact.id);
                            intent.putExtra("dest_cust_phone_number", selectedContact.mobile);
                            intent.putExtra("dest_cust_name", selectedContact.name);
                            intent.putExtra("dest_cust_avatar", selectedContact.avatar_base64);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(ContactCustomerActivity.this, RequestBalanceActivity.class);
                            GCostumer selectedContact = listAdapter.getItemAtPosition(position);
                            intent.putExtra("dest_cust_id", selectedContact.id);
                            intent.putExtra("dest_cust_phone_number", selectedContact.mobile);
                            intent.putExtra("dest_cust_name", selectedContact.name);
                            intent.putExtra("dest_cust_avatar", selectedContact.avatar_base64);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        contactSearchBar = (EditText) findViewById(R.id.contact_search);
        contactSearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                listAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        customerData = new ArrayList<>();

        mPresenter = new ContactCustomerPresenter(this, this);
        getContactList();

    }

    private List<GCostumer> loadCustomerData(){
        List<GCostumer> customerData = new ArrayList<>();
        for(int i=0;i<5;i++){
            GCostumer customer = new GCostumer();
            customer.name = "Ujang Dulag";
            customer.mobile = "081220794742";
            customerData.add(customer);
        }
        return customerData;
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void itemClicked(int position) {

    }

    @Override
    public void itemDeleted(int position) {

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
                    this.contactList.add(phoneNumber);
                }

            }
            phones.close();
            syncContactToServer();
        }


    }

    private void syncContactToServer(){
        if (this.contactList != null && this.contactList.size() > 0){
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String s : this.contactList)
            {
                sb.append(s);
                if (i < this.contactList.size()-1) {
                    sb.append(",");
                }
                i++;
            }
            String mobiles = sb.toString();
//            System.out.println(mobiles);
            //Log.i("kunam", mobiles);
            //Log.i("kunam", String.valueOf(mobiles.length()));
            mPresenter.syncContacts(mobiles);
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
    public void setData(List<GCostumer> customers) {
        customerData = customers;
        listAdapter.setDataCustomer(customerData);
    }
}
