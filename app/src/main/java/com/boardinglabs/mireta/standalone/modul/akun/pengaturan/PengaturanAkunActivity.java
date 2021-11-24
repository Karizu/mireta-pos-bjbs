package com.boardinglabs.mireta.standalone.modul.akun.pengaturan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.Api;
import com.boardinglabs.mireta.standalone.component.network.entities.Ardi.CheckMemberResponse;
import com.boardinglabs.mireta.standalone.component.network.entities.SecurityQuestions;
import com.boardinglabs.mireta.standalone.component.network.response.ApiResponse;
import com.boardinglabs.mireta.standalone.component.util.Loading;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.component.util.Utils;
import com.boardinglabs.mireta.standalone.modul.akun.rfid.TapCard;
import com.boardinglabs.mireta.standalone.modul.akun.ubahpassword.UbahPaswordActivity;
import com.boardinglabs.mireta.standalone.modul.ardi.SaldoActivity;
import com.boardinglabs.mireta.standalone.modul.ardi.registermember.RegisterMember;
import com.boardinglabs.mireta.standalone.modul.home.HomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengaturanAkunActivity extends BaseActivity {

    private Dialog dialog;
    private String UID = "";
    private Context context;
    private List<String> questionList, questionIdList;
    private String spinnerQuestionId = "";
    private Spinner spinners;

    AlertDialog alertTaps;
    LayoutInflater li;
    @SuppressLint("InflateParams")
    TapCard promptsView;
    AlertDialog.Builder alertDialogBuilder;
    @SuppressLint("HandlerLeak")

    public Handler handlerChangePIN = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            UID = bundle.getString("uid");
            Log.d("UID", UID);
            checkMember(UID, "changePIN");
            alertTaps.dismiss();
            promptsView.searchEnd();
        }
    };

    public Handler handlerResetPIN = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            UID = bundle.getString("uid");
            Log.d("UID", UID);
            checkMember(UID, "resetPIN");
            alertTaps.dismiss();
            promptsView.searchEnd();
        }
    };

    public Handler handlerActivatePIN = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            UID = bundle.getString("uid");
            Log.d("UID", UID);
            checkMember(UID, "activatePIN");
            alertTaps.dismiss();
            promptsView.searchEnd();
        }
    };

    @OnClick(R.id.btnKeluar)
    void onClickbtnKeluar(){
        goToLoginPage();
    }

    @OnClick(R.id.btnUbahAkun)
    void onClickbtnUbahAkun(){
        startActivity(new Intent(PengaturanAkunActivity.this, UbahPaswordActivity.class));
    }

    @OnClick(R.id.btnSetPassVoid)
    void onClickSetPass(){
        showDialogs(R.layout.layout_set_pass_void);
        EditText etPasswordVoid = dialog.findViewById(R.id.etPasswordVoid);
        etPasswordVoid.setText(PreferenceManager.getPassVoid());
        Button btnSimpan = dialog.findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            if (etPasswordVoid.getText().toString().equals("")){
                Toast.makeText(context, "Silahkan input password", Toast.LENGTH_SHORT).show();
            } else {
                PreferenceManager.setPassVoid(etPasswordVoid.getText().toString());
                Toast.makeText(context, "Berhasil set password", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btnAktivasiPinKartu)
    void onClickbtnAktivasiPinKartu(){
        li = LayoutInflater.from(context);
        promptsView = (TapCard) li.inflate(R.layout.tap_card, null);

        alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertTaps = alertDialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(alertTaps.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        try {
            promptsView.init(handlerActivatePIN);
            promptsView.searchEnd();
            promptsView.searchBegin();
            promptsView.setOkListener(v1 -> {
                alertTaps.dismiss();
                promptsView.searchEnd();
                ((Activity) context).finish();
            });

            alertTaps.show();
            alertTaps.getWindow().setAttributes(lp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnUbahPinKartu)
    void onClickbtnUbahPinKartu(){
        li = LayoutInflater.from(context);
        promptsView = (TapCard) li.inflate(R.layout.tap_card, null);

        alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertTaps = alertDialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(alertTaps.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        try {
            promptsView.init(handlerChangePIN);
            promptsView.searchEnd();
            promptsView.searchBegin();
            promptsView.setOkListener(v1 -> {
                alertTaps.dismiss();
                promptsView.searchEnd();
                ((Activity) context).finish();
            });

            alertTaps.show();
            alertTaps.getWindow().setAttributes(lp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnResetPinKartu)
    void onClickbtnResetPinKartu(){
        li = LayoutInflater.from(context);
        promptsView = (TapCard) li.inflate(R.layout.tap_card, null);

        alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertTaps = alertDialogBuilder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(alertTaps.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        try {
            promptsView.init(handlerResetPIN);
            promptsView.searchEnd();
            promptsView.searchBegin();
            promptsView.setOkListener(v1 -> {
                alertTaps.dismiss();
                promptsView.searchEnd();
                ((Activity) context).finish();
            });

            alertTaps.show();
            alertTaps.getWindow().setAttributes(lp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_pengaturan_akun;
    }

    @Override
    protected void setContentViewOnChild() {
        ButterKnife.bind(this);
        setToolbarTitle("Pengaturan Akun");
        context = this;
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

    private void checkMember(String UID, String flag) {
        Loading.show(context);
        Api.apiInterface().checkMember(UID, "Bearer " + PreferenceManager.getSessionTokenArdi()).enqueue(new Callback<ApiResponse<CheckMemberResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CheckMemberResponse>> call, Response<ApiResponse<CheckMemberResponse>> response) {
                Loading.hide(context);
                try {
                    if (response.isSuccessful()) {
                        CheckMemberResponse memberResponse = response.body().getData();
                        String member_id = memberResponse.getId();
                        String member_name = memberResponse.getFullname();
                        String member_lulusan = memberResponse.getOpt1();
                        String member_angkatan = memberResponse.getOpt2();
                        String created_at = memberResponse.getCreatedAt();

                        showDialogs(R.layout.layout_aktivasi_pin);
                        TextView tvTitleBarang = dialog.findViewById(R.id.tvTitleBarang);
                        Spinner spinner = dialog.findViewById(R.id.spinner_question);
                        EditText etJawaban = dialog.findViewById(R.id.etJawaban);
                        EditText etPin = dialog.findViewById(R.id.etPin);
                        EditText etNewPin = dialog.findViewById(R.id.etKonfirmasiPin);
                        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

                        if (flag.equals("changePIN")) {
                            tvTitleBarang.setText("CHANGE PIN");
                            spinner.setVisibility(View.GONE);
                            etJawaban.setVisibility(View.GONE);
                            etPin.setHint("Masukkan PIN lama");
                            etNewPin.setHint("Masukkan PIN baru");

                            btnSubmit.setOnClickListener(v -> {
                                dialog.dismiss();
                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("m_id", member_id)
                                        .addFormDataPart("old_pin", Utils.doActivatePin(etPin.getText().toString(), member_id, created_at))
                                        .addFormDataPart("new_pin", Utils.doActivatePin(etNewPin.getText().toString(), member_id, created_at))
                                        .build();
                                doChangePIN(requestBody);
                            });
                        } else if(flag.equals("activatePIN")){
                            //Aktivasi PIN
                            showDialogs(R.layout.layout_aktivasi_pin);
                            Button btnSubmits = dialog.findViewById(R.id.btnSubmit);
                            spinners = dialog.findViewById(R.id.spinner_question);
                            spinner.setPrompt("Pilih pertanyaan keamanan");
                            getSecurityQuestion();
                            EditText etJawabans = dialog.findViewById(R.id.etJawaban);
                            EditText etPins = dialog.findViewById(R.id.etPin);
                            EditText etKonfirmasiPin = dialog.findViewById(R.id.etKonfirmasiPin);

                            btnSubmits.setOnClickListener(v1 -> {
                                if (!etKonfirmasiPin.getText().toString().equals(etPins.getText().toString())){
                                    Toast.makeText(context, "Silahkan periksa kembali pin yang anda masukan", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    RequestBody requestBody = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("security_question_id", spinnerQuestionId)
                                            .addFormDataPart("security_question_answer", etJawabans.getText().toString())
                                            .addFormDataPart("new_pin", Utils.doActivatePin(etPins.getText().toString(), member_id, created_at))
                                            .addFormDataPart("m_id", member_id)
                                            .build();
                                    sendPinEncrypted(Utils.doActivatePin(etPins.getText().toString(), member_id, created_at), requestBody);
//                                    createUserArdi(member_name, );
                                }
                            });
                        } else {
                            tvTitleBarang.setText("RESET PIN");
                            spinner.setVisibility(View.GONE);
                            etJawaban.setVisibility(View.GONE);
                            etPin.setVisibility(View.GONE);
                            etNewPin.setHint("Masukkan PIN baru");

                            btnSubmit.setOnClickListener(v -> {
                                dialog.dismiss();
                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("m_id", member_id)
                                        .addFormDataPart("new_pin", Utils.doActivatePin(etNewPin.getText().toString(), member_id, created_at))
                                        .build();
                                doResetPIN(requestBody);
                            });
                        }

                    } else {
                        if (response.message().equals("Unauthorized")) {
                            Toast.makeText(PengaturanAkunActivity.this, "Unauthorized, silahkan login ulang", Toast.LENGTH_SHORT).show();
                            ImageView btnClose = dialog.findViewById(R.id.btnClose);
                            btnClose.setOnClickListener(v -> dialog.dismiss());
                            Button btnRelogin = dialog.findViewById(R.id.btnRelogin);
                            btnRelogin.setOnClickListener(v -> goToLoginPage());
                        }

                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        try {
                            Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CheckMemberResponse>> call, Throwable t) {
                Loading.hide(context);
                t.printStackTrace();
            }
        });
    }

    private void createUserArdi() {
        Loading.show(context);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fullname", "")
                .addFormDataPart("username", "")
                .addFormDataPart("password", "")
                .build();
        Api.apiInterface().createUserArdi(requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    Loading.hide(context);
                    if (response.isSuccessful()){

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

    private void sendPinEncrypted(String encryptedText, RequestBody requestBody) {
        if (encryptedText.equals("") || spinnerQuestionId.equals("")) {
            Toast.makeText(context, "Terjadi kesalahan pada saat memproses pin", Toast.LENGTH_SHORT).show();
        } else {
            Loading.show(context);
            try {
                Api.apiInterface().doActivatePin(requestBody, "Bearer " + PreferenceManager.getSessionTokenArdi()).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Loading.hide(context);
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Aktivasi PIN berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PengaturanAkunActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            if (response.message().equals("Unauthorized")) {
                                showDialogs(R.layout.dialog_session_expired);
                                ImageView btnClose = dialog.findViewById(R.id.btnClose);
                                btnClose.setOnClickListener(v -> dialog.dismiss());
                                Button btnRelogin = dialog.findViewById(R.id.btnRelogin);
                                btnRelogin.setOnClickListener(v -> {
                                    goToLoginPage();
                                });
                            }

                            try {
                                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                if (jObjError.getString("error").startsWith("Balance not")) {
                                    Toast.makeText(context, "Saldo anda kurang, silahkan isi saldo anda terlebih dahulu", Toast.LENGTH_LONG).show();
                                } else if (jObjError.getString("error").startsWith("Invalid Data: 01")){
                                    Toast.makeText(context, "PIN Salah", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Terjadi kesalahan saat aktivasi", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Loading.hide(context);
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getSecurityQuestion() {
        Loading.show(context);

        questionList = new ArrayList<>();
        questionIdList = new ArrayList<>();
        questionList.add("Pilih pertanyaan keamanan");
        questionIdList.add("0");
        Api.apiInterface().getListQuestion("Bearer " + PreferenceManager.getSessionTokenArdi()).enqueue(new Callback<ApiResponse<List<SecurityQuestions>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SecurityQuestions>>> call, Response<ApiResponse<List<SecurityQuestions>>> response) {
                Loading.hide(context);
                try {
                    List<SecurityQuestions> res = response.body().getData();
                    for (int i = 0; i < res.size(); i++) {
                        SecurityQuestions users = res.get(i);
                        questionList.add(users.getQuestion());
                        questionIdList.add(users.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<SecurityQuestions>>> call, Throwable t) {
                Loading.hide(context);
                t.printStackTrace();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PengaturanAkunActivity.this, R.layout.layout_spinner_text, questionList){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        spinners.setAdapter(dataAdapter);

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    spinnerQuestionId = questionIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doChangePIN(RequestBody requestBody) {
        Loading.show(context);
        try {
            Api.apiInterface().changePIN(requestBody, "Bearer "+PreferenceManager.getSessionTokenArdi()).enqueue(new Callback<ApiResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Loading.hide(context);
                    if (response.isSuccessful()){
                        showDialogs(R.layout.layout_sukses_register_member);
                        Button btnOK = dialog.findViewById(R.id.btnOK);
                        TextView tvTitleBarang = dialog.findViewById(R.id.tvTitleBarang);
                        TextView tvDesc = dialog.findViewById(R.id.etMasterKey);
                        tvTitleBarang.setText("CHANGE PIN");
                        tvDesc.setText("Berhasil ganti pin kartu");
                        btnOK.setOnClickListener(v -> dialog.dismiss());
                    } else {
                        if (response.message().equals("Unauthorized")) {
                            showDialogs(R.layout.dialog_session_expired);
                            ImageView btnClose = dialog.findViewById(R.id.btnClose);
                            btnClose.setOnClickListener(v -> dialog.dismiss());
                            Button btnRelogin = dialog.findViewById(R.id.btnRelogin);
                            btnRelogin.setOnClickListener(v -> {
                                goToLoginPage();
                            });
                        }

                        try {
                            showDialogs(R.layout.layout_wrong_pass);
                            Button btnOK = dialog.findViewById(R.id.btnOK);
                            TextView tvTitleBarang = dialog.findViewById(R.id.tvTitleBarang);
                            TextView etMasterKey = dialog.findViewById(R.id.etMasterKey);
                            btnOK.setOnClickListener(v -> dialog.dismiss());

                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            if (jObjError.getString("error").startsWith("Balance not")) {
                                tvTitleBarang.setText("SALDO KURANG");
                                etMasterKey.setText("Saldo anda kurang, silahkan isi saldo anda terlebih dahulu");
                            } else if (jObjError.getString("error").startsWith("Invalid Data: 01")){
                                tvTitleBarang.setText("PIN SALAH");
                                etMasterKey.setText("PIN anda salah, silahkan periksa kembali PIN anda");
                            } else {
                                tvTitleBarang.setText("WARNING");
                                etMasterKey.setText(jObjError.getString("error"));
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Loading.hide(context);
                    t.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doResetPIN(RequestBody requestBody) {
        Loading.show(context);
        try {
            Api.apiInterface().resetPIN(requestBody, "Bearer "+PreferenceManager.getSessionTokenArdi()).enqueue(new Callback<ApiResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Loading.hide(context);
                    if (response.isSuccessful()){
                        showDialogs(R.layout.layout_sukses_register_member);
                        Button btnOK = dialog.findViewById(R.id.btnOK);
                        TextView tvTitleBarang = dialog.findViewById(R.id.tvTitleBarang);
                        TextView tvDesc = dialog.findViewById(R.id.etMasterKey);
                        tvTitleBarang.setText("RESET PIN");
                        tvDesc.setText("Berhasil reset pin kartu");
                        btnOK.setOnClickListener(v -> dialog.dismiss());
                    } else {
                        if (response.message().equals("Unauthorized")) {
                            showDialogs(R.layout.dialog_session_expired);
                            ImageView btnClose = dialog.findViewById(R.id.btnClose);
                            btnClose.setOnClickListener(v -> dialog.dismiss());
                            Button btnRelogin = dialog.findViewById(R.id.btnRelogin);
                            btnRelogin.setOnClickListener(v -> {
                                goToLoginPage();
                            });
                        }

                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            if (jObjError.getString("error").startsWith("Balance not")) {
                                Toast.makeText(context, "Saldo anda kurang, silahkan isi saldo anda terlebih dahulu", Toast.LENGTH_LONG).show();
                            } else if (jObjError.getString("error").startsWith("Invalid Data: 01")){
                                Toast.makeText(context, "PIN Salah", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Loading.hide(context);
                    t.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDialogs(int layout) {
        dialog = new Dialog(Objects.requireNonNull(PengaturanAkunActivity.this));
        //set content
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
