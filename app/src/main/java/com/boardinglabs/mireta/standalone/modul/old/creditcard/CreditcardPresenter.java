package com.boardinglabs.mireta.standalone.modul.old.creditcard;

/**
 * Created by Dhimas on 2/2/18.
 */

public interface CreditcardPresenter {
    void onTransaction(String charge, String totalAmount, String note, boolean activeImage,
                       String amount, String voucherId, String voucherAmount);

    void onFastTransaction(String charge, String totalAmount, String note, boolean activeImage,
                           String amount, String voucherId, String voucherAmount, String name,
                           String email, String phone, String merhcnatId, String qrId,
                           String cardHash, String cardType);

    void addCreditcard(String tokenId, String cardhash, String cardName, String expiryMonth, String expiryYear, String expiredToken);

    void calculateAmount(String amount, String type, String merchantId);

    void setDefault(String tokenId);
}
