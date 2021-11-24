package com.boardinglabs.mireta.standalone.modul.old.jiwasraya;

import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;

import org.json.JSONException;

/**
 * Created by Dhimas on 12/23/17.
 */

public interface JiwasrayaView {
    void onSuccessEdit();
    void onSuccessInquiry(GTransaction transaction) throws JSONException;
    void onSuccessUpdateAmountInquiry(GTransaction transaction) throws JSONException;
}
