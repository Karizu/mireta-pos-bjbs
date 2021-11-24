package com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard;

import android.widget.EditText;

import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.component.util.TextWatcherAdapter;

/**
 * Created by Dhimas on 2/2/18.
 */

public class InputCreditcardPresenterImpl implements InputCreditcardPresenter {

    public InputCreditcardPresenterImpl() {

    }

    @Override
    public void reformatCardNumber(String current, EditText editText, TextWatcherAdapter textWatcherAdapter) {
        String text = editText.getText().toString();
        if (!text.equals(current)) {
            editText.removeTextChangedListener(textWatcherAdapter);

            String formatted = MethodUtil.formatCardNumber(text);

            editText.setText(formatted);
            editText.setSelection(formatted.length());

            editText.addTextChangedListener(textWatcherAdapter);
        }
    }
}
