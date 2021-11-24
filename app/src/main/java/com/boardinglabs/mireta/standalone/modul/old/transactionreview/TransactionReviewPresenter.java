package com.boardinglabs.mireta.standalone.modul.old.transactionreview;

/**
 * Created by Dhimas on 12/8/17.
 */

public interface TransactionReviewPresenter {
    void onRegisterPremium(String refferalId);

    void getBalance();

    void payTransaction(String transactionId);

    void checkRefferal(String code);

    void calculate(String amount, String type, String merchantId);

    void chargeTransaction(String transactionId);
}
