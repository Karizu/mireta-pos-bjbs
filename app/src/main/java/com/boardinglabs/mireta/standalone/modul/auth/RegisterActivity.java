package com.boardinglabs.mireta.standalone.modul.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.Api;
import com.boardinglabs.mireta.standalone.component.network.ApiLocal;
import com.boardinglabs.mireta.standalone.component.network.response.ApiResponse;
import com.boardinglabs.mireta.standalone.component.util.Loading;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Context context;

    @BindView(R.id.input_fullname)
    EditText input_fullname;
    @BindView(R.id.input_username)
    EditText input_username;
    @BindView(R.id.input_password)
    EditText input_password;
    @BindView(R.id.input_email)
    EditText input_email;
    @BindView(R.id.input_store_name)
    EditText input_store_name;
    @BindView(R.id.input_brand_name)
    EditText input_brand_name;
    @BindView(R.id.input_registration_email)
    EditText input_registration_email;
    @BindView(R.id.input_store_address)
    EditText input_store_address;

    @OnClick(R.id.btn_register)
    void onClickRegister(){
        doValidate();
    }

    @OnClick(R.id.tvLogin)
    void onClickTvLogin(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        context = this;
    }

    private void doRegister(){
        Loading.show(context);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", input_username.getText().toString())
                .addFormDataPart("email", input_email.getText().toString())
                .addFormDataPart("password", input_password.getText().toString())
                .addFormDataPart("fullname", input_fullname.getText().toString())
                .addFormDataPart("registration_email", input_registration_email.getText().toString())
                .addFormDataPart("brand_name", input_brand_name.getText().toString())
                .addFormDataPart("store_name", input_store_name.getText().toString())
                .addFormDataPart("store_address", input_store_address.getText().toString())
                .build();

        ApiLocal.apiInterface().registerUser(requestBody, "Bearer "+ PreferenceManager.getSessionToken()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Loading.hide(context);
                try {
                    if (response.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username", input_username.getText().toString());
                        intent.putExtra("password", input_password.getText().toString());
                        startActivity(intent);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Loading.hide(context);
                t.printStackTrace();
            }
        });
    }

    private void doRegisterArdi(){
        Loading.show(context);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", input_username.getText().toString())
                .addFormDataPart("email", input_email.getText().toString())
                .addFormDataPart("password", input_password.getText().toString())
                .addFormDataPart("fullname", input_fullname.getText().toString())
                .build();

        Api.apiInterface().registerUserArdi(requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Loading.hide(context);
                try {
                    if (response.isSuccessful()){
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.putExtra("username", input_username.getText().toString());
//                        intent.putExtra("password", input_password.getText().toString());
//                        startActivity(intent);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Loading.hide(context);
                t.printStackTrace();
            }
        });
    }

    private void doValidate(){
        if (input_email.getText().toString().equals("") || input_username.getText().toString().equals("")
                || input_brand_name.getText().toString().equals("") || input_fullname.getText().toString().equals("")
        || input_password.getText().toString().equals("") || input_registration_email.getText().toString().equals("")
        || input_store_address.getText().toString().equals("") || input_store_name.getText().toString().equals("")){
            Toast.makeText(context, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            doRegisterArdi();
            doRegister();
        }
    }
}
