package com.boardinglabs.mireta.standalone.modul.old.register.passcode;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 11/23/17.
 */

public class ReenterPasscodeFragment extends Fragment {
    private OnCompleteReenterCode onReenterCode;
    public static final String PASSCODE = "passcode";
    private String tempCode;
    private EditText passwordInput;
    private RelativeLayout bottomMenu;
    private TextView toolbarTitle;
    private ImageView backButton;
    private CircleImageView profileImage;
    private TextView hintText;
    boolean isForgot;

    public ReenterPasscodeFragment newInstance(String beforeCode, boolean isForgot) {
        ReenterPasscodeFragment fragment = new ReenterPasscodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PASSCODE, beforeCode);
        bundle.putBoolean("is_forgot", isForgot);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setReenterCodeListener(OnCompleteReenterCode onCompleteReenterCode) {
        onReenterCode = onCompleteReenterCode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passcode_layout, container, false);
        tempCode = getArguments().getString(PASSCODE);
        bottomMenu = (RelativeLayout) view.findViewById(R.id.bottom_menu);
        toolbarTitle = (TextView) view.findViewById(R.id.hometoolbar_title);
        profileImage = (CircleImageView) view.findViewById(R.id.profile);
        backButton = (ImageView) view.findViewById(R.id.backButton);
        hintText = (TextView) view.findViewById(R.id.hint_text);

//        if (!PreferenceManager.getStatusAkupay()) {
//            reenterImg.setImageDrawable(getResources().getDrawable(R.drawable.pasy_agent));
//        }

        initComponent(view);
        initEvent();
        return view;
    }

    private void initComponent(View view) {
        TextView greetingTxt = (TextView) view.findViewById(R.id.greeting_text);
        isForgot = getArguments().getBoolean("is_forgot", false);
        if (isForgot) {
            toolbarTitle.setText("KONFIRMASI KODE PIN");
            greetingTxt.setText("Konfirmasi ulang kode PIN akunmu untuk keamanan.");
            greetingTxt.setVisibility(View.INVISIBLE);
            profileImage.setVisibility(View.INVISIBLE);
            bottomMenu.setVisibility(View.GONE);
            hintText.setText("Konfirmasi ulang kode PIN akunmu untuk keamanan.");
        } else {
            toolbarTitle.setText("KONFIRMASI KODE PIN");
            greetingTxt.setText("KONFIRMASI PIN PENGAMAN");
            greetingTxt.setVisibility(View.INVISIBLE);
            profileImage.setVisibility(View.INVISIBLE);
            hintText.setText("Konfirmasi ulang kode PIN akunmu untuk keamanan.");
            bottomMenu.setVisibility(View.GONE);
        }

        passwordInput = (EditText) view.findViewById(R.id.pin_input);
//        passwordInput.setTransformationMethod(new MyPasswordTransformationMethod());

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){
                    if (onReenterCode != null) {
                        if (tempCode.equalsIgnoreCase(s.toString())) {
                            onReenterCode.onMatchCode(s.toString());
                        } else {
                            onReenterCode.onNotMatchCode();
                        }
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(s.length()>0) {
                        passwordInput.setLetterSpacing(1.0f);
                    }else{
                        passwordInput.setLetterSpacing(0.0f);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

            backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPresssd();
            }
        });
    }

    private void onBackPresssd(){
        //((PasscodeActivity)getActivity()).popFragmentOnTop();
        Toast.makeText(getContext(),"Anda tidak dapat kembali dari halaman ini, harap selesaikan proses konfirmasi",Toast.LENGTH_LONG).show();
    }

    private void initEvent() {

    }



    public interface OnCompleteReenterCode{
        void onMatchCode(String code);
        void onNotMatchCode();
    }
}
