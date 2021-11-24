package com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard;

import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;

/**
 * Created by Dhimas on 2/4/18.
 */

public interface InputCreditcardView {
    void onSuccessTransactionMidtrans(GCreditCard gCreditCard);

    void setTotalAmount(String totalAmount);

    void onSuccessTransaction();
}
