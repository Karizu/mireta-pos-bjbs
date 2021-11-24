package com.boardinglabs.mireta.standalone.modul.old.creditcard.addcreditcard;

/**
 * Created by Dhimas on 2/18/18.
 */

public interface AddCreditcardPresenter {
    void addCreditcard(String cardhash, String savedTokenId, String expiryMonth, String expiryYear);
}
