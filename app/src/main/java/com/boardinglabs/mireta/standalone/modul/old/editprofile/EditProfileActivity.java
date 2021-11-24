package com.boardinglabs.mireta.standalone.modul.old.editprofile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.mayanknagwanshi.imagepicker.imageCompression.ImageCompressionListener;
import in.mayanknagwanshi.imagepicker.imagePicker.ImagePicker;
import rx.functions.Action1;

/**
 * Created by Dhimas on 12/23/17.
 */

public class EditProfileActivity extends BaseActivity implements CommonInterface, EditProfileView{
    private EditText textName;
    private EditText textMobile;
    private EditText textEmail;
    private EditText textLastName;
    private EditText textUsername;
    private EditText textAddress;
    private EditText textBirthDate;
    private Switch genderSwtich;
    private TextView genderSwtichLabel;
    private CircleImageView avatarImageView;
    private ImageView avatarChangeImageView;
    private Button changeAvatarBtn;
    private String base64Avatar;
    private Calendar calendar;
    private ImagePicker imagePicker;
    private Button updateBtn;
    private EditProfilePresenter mPresenter;
    private LinearLayout jenisKelaminCollapsible;
    private RadioGroup radioSex;
    private ImageView radioSexSign;
    private RadioButton radioMale;
    private TextView kelaminText;
    private Button submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImagePicker.setMinQuality(600, 600);
        imagePicker = new ImagePicker();
        requestAppPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            //Add compression listener if withCompression is set to true
            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompressed(String filePath) {//filePath of the compressed image
                    //convert to bitmap easily
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap != null) {
                        avatarImageView.setImageBitmap(bitmap);
                        base64Avatar = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 50);
                    }
                }
            });
        }
//        //call the method 'getImageFilePath(Intent data)' even if compression is set to false
        try {
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {//filePath will return null if compression is set to true
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                if (bitmap != null) {
                    avatarImageView.setImageBitmap(bitmap);

                    base64Avatar = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 50);
                }
            }
        }
        catch (Exception e) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

//    public void onPickImage(View view) {
//        // Click on image button
//        ImagePicker.pickImage(this, "Pilih avatar");
//    }

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
    protected int getLayoutResourceId() {
        return R.layout.edit_profile_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitleWithSubmitButton("Ubah Akun");
        textName = findViewById(R.id.register_name);
        textMobile = findViewById(R.id.register_mobile);
        textEmail = findViewById(R.id.register_email);
        textLastName = findViewById(R.id.register_last_name);
        textUsername = findViewById(R.id.register_username);
        genderSwtich = findViewById(R.id.switch_gender);
        genderSwtichLabel = findViewById(R.id.gender_label);
        textBirthDate = findViewById(R.id.register_birthdate);
        changeAvatarBtn = findViewById(R.id.change_picture_btn);
        avatarImageView = findViewById(R.id.avatar_img);
        textAddress = findViewById(R.id.register_address);
        jenisKelaminCollapsible = findViewById(R.id.jenis_kelamin_collaps);
        radioSex = findViewById(R.id.radioSex);
        radioSexSign = findViewById(R.id.radioSexSign);
        radioMale = findViewById(R.id.radioMale);
        kelaminText = findViewById(R.id.kelamin_display);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setVisibility(View.GONE);
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdateLabel();
            }
        };


        textBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        updateBtn = (Button) findViewById(R.id.logout);
        RxView.clicks(updateBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PreferenceManager.logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//                logoutAction(false);
                goToLoginPage();
            }
        });

        jenisKelaminCollapsible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioSex.getVisibility() == View.GONE){
                    radioSex.setVisibility(View.VISIBLE);
                    radioSexSign.setImageDrawable(context.getResources().getDrawable(R.drawable.keyboard_arrow_up_24px));
                    kelaminText.setText(radioMale.isChecked()?"Laki-laki":"Perempuan");
                }else{
                    radioSex.setVisibility(View.GONE);
                    radioSexSign.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24));
                    kelaminText.setText("Jenis Kelamin");
                }
            }
        });

        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioSex.getVisibility() == View.VISIBLE) {
                    kelaminText.setText(radioMale.isChecked() ? "Laki-laki" : "Perempuan");
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateBirthdateLabel() {
        submitButton.setVisibility(View.VISIBLE);
        String format = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        textBirthDate.setText(sdf.format(calendar.getTime()));
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new EditProfilePresenterImpl(this, this);
        GAgent agent = PreferenceManager.getAgent();
        textName.setText(agent.name);
        textMobile.setText(agent.mobile);
        textEmail.setText(agent.email);
        textLastName.setText(agent.last_name);
        textUsername.setText(agent.username);
        genderSwtich.setChecked((agent.gender != null) ? agent.gender.equals("M") : false);
        genderSwtichLabel.setText(agent.gender != null && agent.gender.equals("M") ? "Laki-laki" : "Perempuan");
        radioMale.setChecked((agent.gender != null) ? agent.gender.equals("M") : true);
        textBirthDate.setText(agent.birthdate);
        textAddress.setText(agent.address);
        if (agent.birthdate != null && agent.birthdate.length()>0){
            calendar.set(Integer.parseInt(agent.birthdate.split("-")[0]),Integer.parseInt(agent.birthdate.split("-")[1])-1,Integer.parseInt(agent.birthdate.split("-")[2]));
        }
        if (agent.avatar_base64 != null && !agent.avatar_base64.equals("")){
            base64Avatar = agent.avatar_base64;
            byte[] decodedString = Base64.decode(agent.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImageView.setImageBitmap(decodedByte);
        }

        RxView.clicks(changeAvatarBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasReadPermissions() && hasWritePermissions()){
                    submitButton.setVisibility(View.VISIBLE);
                    imagePicker.withActivity(EditProfileActivity.this) //calling from activity
                            .withCompression(true)
                            .chooseFromGallery(true) //default is true
                            .chooseFromCamera(true) //default is true
                            .start();
                }
                else{
                    requestAppPermissions();
                }
            }
        });

        textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



//        RxView.clicks(updateBtn).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                mPresenter.updateProfile(textName.getText().toString(),
//                        textMobile.getText().toString(),
//                        textEmail.getText().toString());
//            }
//        });
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 12345); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {
//        String name, String mobile, String email, String username, String lastName, String gender, String address, String birthdate, String avatarbase64
        mPresenter.updateProfile(textName.getText().toString(),
                textMobile.getText().toString(),
                textEmail.getText().toString(),
                textUsername.getText().toString(),
                textLastName.getText().toString(),
                (radioMale.isChecked() ? "M" : "W"),
                textAddress.getText().toString(),
                textBirthDate.getText().toString(),
                (base64Avatar != null ? base64Avatar : ""));
    }

    @Override
    public void onSuccessEdit() {
//        MethodUtil.showCustomToast(this, "Profile berhasil di update", R.drawable.success);
//        Intent intent = new Intent(this, HomePageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("openAgent", true);
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }
}
