package com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard;

import android.widget.EditText;

import com.boardinglabs.mireta.standalone.component.util.TextWatcherAdapter;

/**
 * Created by Dhimas on 2/2/18.
 */

public interface InputCreditcardPresenter {
    void reformatCardNumber(String current, EditText editText, TextWatcherAdapter textWatcherAdapter);
}
