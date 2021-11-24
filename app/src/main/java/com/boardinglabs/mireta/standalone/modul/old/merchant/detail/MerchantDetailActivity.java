package com.boardinglabs.mireta.standalone.modul.old.merchant.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.scanqr.ScanQRActivity;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantDetailActivity extends BaseActivity implements CommonInterface, MerchantDetailView, OnMapReadyCallback {
    private MerchantDetailPresenter mpresenter;
    private CircularImageView mProfileImage;
    //    private OpenSansBoldTextView mLableFavorite;
    private ToggleButton mBtnAddFavorite;
    private TextView mMerchantName;
    private TextView mMerchantDescription;
    //    private OpenSansRegularTextView mMerchantEmail;
//    private OpenSansRegularTextView mMerchantPhone;
    private TextView mMerchaneNameBelow;
    private TextView mMerchantAddress;
    private Button mBtnNextPayment;
    private LinearLayout containerCall;
    private TextView mPhoneNumber;
    //private LinearLayout mDirection;
    private TextView popularityText;
    private ImageView badgeImage;
    private ImageView callButton;
    private String merchantCode;
    //private LinearLayout instagram;
    //private LinearLayout whatsapp;
    private TextView productMerchant;
    private TextView typeMerchant;
    private TextView infoMerchant;
    private TextView rangePrice;
    private ImageView layoutMerchant;
    private GMerchant merchantData;
    private ImageView bayarButton;

    @Override

    protected int getLayoutResourceId() {
        return R.layout.partial_merchant_detail;

    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Merchant");
        merchantCode = getIntent().getStringExtra("merchantCode");
        initComponent();
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mpresenter = new MerchantDetailPresenterImpl(this, this);
        String code = getIntent().getStringExtra("merchantCode");
        mpresenter.getDetail(code);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

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
    public void onGetMerchantDetail(GMerchant data) {
        merchantData = data;
        mMerchantName.setText(!TextUtils.isEmpty(data.name) ? data.name : "");
        mMerchantDescription.setText(!TextUtils.isEmpty(data.description) ? data.description : "");
//        mMerchantEmail.setText(!TextUtils.isEmpty(data.getEmail()) ? data.getEmail() : "");
//        mMerchantPhone.setText(!TextUtils.isEmpty(data.getPhone()) ? data.getPhone() : "");
        mMerchaneNameBelow.setText(mMerchantName.getText().toString());
        mMerchantAddress.setText(data.address);
        if (data.is_favorite) {
            mBtnAddFavorite.setChecked(data.is_favorite);
            mBtnAddFavorite.setTextColor(Color.WHITE);
            mBtnAddFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_full_blue));
        }
        mPhoneNumber.setText(data.mobile);
        if (data.is_premium) {
            badgeImage.setVisibility(View.VISIBLE);
        }
        if (data.is_instant) {
            badgeImage.setVisibility(View.VISIBLE);
            badgeImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.badge_instant_merchant));
        }
        productMerchant.setText(data.product);
        infoMerchant.setText(data.info);
        String rangePriceTxt = "Rp " + MethodUtil.toCurrencyFormat(data.price_range_start + "") +
                " - " + "Rp " + MethodUtil.toCurrencyFormat(data.price_range_end + "");
        rangePrice.setText(rangePriceTxt);
        String resultType = "";
        List<String> type = new ArrayList<>();
        if (data.is_offline != null && data.is_offline.equalsIgnoreCase("1")) {
            type.add("Offline Shop");
        }
        if (data.is_online != null && data.is_online.equalsIgnoreCase("1")) {
            type.add("Online Shop");
        }
        if (data.is_service != null && data.is_service.equalsIgnoreCase("1")) {
            type.add("Service");
        }
        for (String typen : type) {
            resultType = resultType + typen;
            if (type.size() > 1 && type.indexOf(typen) != type.size()) {
                resultType = resultType + ", ";
            }
        }
        typeMerchant.setText(resultType);
        if (data.avatars != null) {
            Glide.with(this).load(data.avatars.base_url + "/" + data.avatars.path).placeholder(R.drawable.default_image_merchant).fitCenter().dontAnimate().into(mProfileImage);
        }

        if (data.banners != null) {
            Glide.with(this).load(data.banners.base_url + "/" + data.banners.path).placeholder(R.drawable.default_layout_merchant).centerCrop().dontAnimate().into(layoutMerchant);
        }

        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.merchant_maps);
        mSupportMapFragment.getMapAsync(this);
    }

    private void initComponent() {
        callButton = (ImageView) findViewById(R.id.callButton);
        mProfileImage = (CircularImageView) findViewById(R.id.frgMerchantDetail_profilePicture);
//        mLableFavorite = (OpenSansBoldTextView) view.findViewById(R.id.frgMerchantDetail_labelFavorite);
        mBtnAddFavorite = (ToggleButton) findViewById(R.id.frgMerchantDetail_btnFavorite);
        mMerchantName = (TextView) findViewById(R.id.frgMerchantDetail_merchantName);
        mMerchantDescription = (TextView) findViewById(R.id.frgMerchantDetail_merchantDescription);
//        mMerchantEmail = (OpenSansRegularTextView) view.findViewById(R.id.frgMerchantDetail_merchantEmail);
//        mMerchantPhone = (OpenSansRegularTextView) view.findViewById(R.id.frgMerchantDetail_merchantPhone);
        mMerchaneNameBelow = (TextView) findViewById(R.id.frgMerchantDetail_merchantNameBelow);
        mMerchantAddress = (TextView) findViewById(R.id.frgMerchantDetail_merchantDescriptionBelow);
        containerCall = (LinearLayout) findViewById(R.id.container_all);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        //mDirection = (LinearLayout) findViewById(R.id.direction_btn);
        popularityText = (TextView) findViewById(R.id.popularity_text);
        badgeImage = (ImageView) findViewById(R.id.badge_premium);
        //instagram = (LinearLayout) findViewById(R.id.instagram);
        //whatsapp = (LinearLayout) findViewById(R.id.whatsapp);
        productMerchant = (TextView) findViewById(R.id.product_merchant);
        typeMerchant = (TextView) findViewById(R.id.type_merchant);
        infoMerchant = (TextView) findViewById(R.id.info_merchant);
        rangePrice = (TextView) findViewById(R.id.range_price);
        layoutMerchant = (ImageView) findViewById(R.id.layout_image_user);
//        mBtnEmail = (ImageView) view.findViewById(R.id.frgMerchantDetail_btnMail);
//        mBtnPhone = (ImageView) view.findViewById(R.id.frgMerchantDetail_btnPhone);
//        mBtnChat = (ImageView) view.findViewById(R.id.frgMerchantDetail_btnChat);
        bayarButton = (ImageView) findViewById(R.id.bayarButton);
        bayarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MerchantDetailActivity.this,
                        ScanQRActivity.class));
            }
        });
    }

    private void initEvent() {
        RxCompoundButton.checkedChanges(mBtnAddFavorite).skip(1).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isChecked) {
                if (isChecked) {
                    mBtnAddFavorite.setTextColor(Color.WHITE);
//                    mPresenter.addFavoritMerchant();
                    mBtnAddFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_full_blue));
                } else {
                    mBtnAddFavorite.setTextColor(Color.BLACK);
//                    mPresenter.removeFavoritMerchant();
                    mBtnAddFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
                }
            }
        });

        RxView.clicks(containerCall).subscribe(new Action1<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void call(Void aVoid) {
                final String phone = mPhoneNumber.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        RxView.clicks(callButton).subscribe(new Action1<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void call(Void aVoid) {
                final String phone = mPhoneNumber.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

//        RxView.clicks(whatsapp).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                try {
//                    String phone = mPhoneNumber.getText().toString();
//                    Character firstChar = phone.charAt(0);
//                    if (firstChar.toString().equalsIgnoreCase("0")) {
//                        phone = "62" + phone.substring(1);
//                    }
//                    String url = "https://api.whatsapp.com/send?phone=" + phone;
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                } catch (Exception e) {
//                    MethodUtil.showCustomToast(MerchantDetailActivity.this, "Gagal membuka Whatsapp", R.drawable.ic_error_login);
//                    e.printStackTrace();
//                }
//            }
//        });

//        RxView.clicks(instagram).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
////                String instagramUser = mPresenter.getMerchant().getWebsite();
////                Intent intent = new Intent(getActivity(), InstagramWebViewActivity.class);
////                intent.putExtra("instagram", instagramUser);
////                startActivity(intent);
//                Toast.makeText(getBaseContext(), "Under Construction", Toast.LENGTH_SHORT).show();
//            }
//        });

//        RxView.clicks(mDirection).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Double lat = 0.0;
//                Double lon = 0.0;
//                if (merchantData != null) {
//                    lat = Double.parseDouble(merchantData.gmap_lat);
//                    lon = Double.parseDouble(merchantData.gmap_long);
//                }
//                String url = String.format("http://maps.google.com/maps?daddr=%s,%s", lat + "", lon + "");
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse(url));
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            newPhoneIntent(mPhoneNumber.getText().toString());
        }
    }

    private void newPhoneIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(getBaseContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (merchantData != null && merchantData.gmap_lat != null && merchantData.gmap_long != null) {
            Double lat = Double.parseDouble(merchantData.gmap_lat);
            Double lon = Double.parseDouble(merchantData.gmap_long);
            LatLng locations = new LatLng(lat, lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations, 12.4f));
            Marker addMarker = googleMap.addMarker(new MarkerOptions().position(locations));
            addMarker.setTitle(merchantData.name);
            addMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        } else {
            Toast.makeText(this, "Tidak dapat membuka map untuk saat ini", Toast.LENGTH_SHORT).show();
        }

    }
}
