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

import com.boardinglabs.mireta.standalone.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 11/15/17.
 */

public class EnterPasscodeFragment extends Fragment {
    private static final String TAG = "TAG";

    private EditText passwordInput;
    private TextView greetingTxt;
    private CircleImageView profileImage;
    private RelativeLayout bottomMenu;
    private OnCompleteInput onCompleteInput;

    private TextView toolbarTitle;
    private ImageView backButton;
    private TextView hintText;

    public static EnterPasscodeFragment newInstance(boolean isForgot,boolean isRegister) {
        EnterPasscodeFragment fragment = new EnterPasscodeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_forgot", isForgot);
        bundle.putBoolean("is_register", isRegister);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnCompleteListener(OnCompleteInput onComplete) {
        onCompleteInput = onComplete;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passcode_layout, container, false);
        greetingTxt = (TextView) view.findViewById(R.id.greeting_text);
        bottomMenu = (RelativeLayout) view.findViewById(R.id.bottom_menu);
        toolbarTitle = (TextView) view.findViewById(R.id.hometoolbar_title);
        profileImage = (CircleImageView) view.findViewById(R.id.profile);
        backButton = (ImageView) view.findViewById(R.id.backButton);
        hintText = (TextView) view.findViewById(R.id.hint_text);

        boolean isForgot = getArguments().getBoolean("is_forgot", false);
        if (isForgot) {
            toolbarTitle.setText("ATUR KODE PIN");
            greetingTxt.setText("");
            greetingTxt.setVisibility(View.INVISIBLE);
            bottomMenu.setVisibility(View.INVISIBLE);
            profileImage.setVisibility(View.INVISIBLE);
            hintText.setText("Atur kode PIN akunmu kembali untuk keamanan.");
        } else {
            toolbarTitle.setText("ATUR KODE PIN");
            greetingTxt.setText("");
            greetingTxt.setVisibility(View.INVISIBLE);
            profileImage.setVisibility(View.INVISIBLE);
            bottomMenu.setVisibility(View.GONE);
            hintText.setText("Atur kode PIN akunmu kembali untuk keamanan.");

        }


        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        passwordInput = (EditText) view.findViewById(R.id.pin_input);
        passwordInput.setText("");
//        passwordInput.setTransformationMethod(new MyPasswordTransformationMethod());

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){
                    onCompleteInput.setCode(s.toString());
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
        getActivity().onBackPressed();
    }

    public interface OnCompleteInput{
        void setCode(String code);
    }
}
