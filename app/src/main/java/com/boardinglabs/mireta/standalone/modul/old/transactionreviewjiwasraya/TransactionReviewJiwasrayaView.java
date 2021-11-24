package com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya;

import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import org.json.JSONException;

/**
 * Created by Dhimas on 12/7/17.
 */

public interface TransactionReviewJiwasrayaView {
    void onSuccessGetBalance(String balance);

    void onSuccessRegisterPremium(MessageResponse mResponse);

    void onSuccessPayTransactionJiwasraya(GTransaction transaction) throws JSONException;

    void onSuccessCheckReferral(String refferalId);

    void chargeAmount(String totalAmount, String fee);

//    void charge(QRTransactionResponse json);
}
