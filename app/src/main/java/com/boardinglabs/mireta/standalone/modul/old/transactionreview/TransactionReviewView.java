package com.boardinglabs.mireta.standalone.modul.old.transactionreview;

import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.QRTransactionResponse;

/**
 * Created by Dhimas on 12/7/17.
 */

public interface TransactionReviewView {
    void onSuccessGetBalance(String balance);

    void onSuccessRegisterPremium(MessageResponse mResponse);

    void onSuccessPayTransaction(GTransaction transaction);

    void onSuccessCheckReferral(String refferalId);

    void chargeAmount(String totalAmount, String fee);

    void charge(QRTransactionResponse json);
}
