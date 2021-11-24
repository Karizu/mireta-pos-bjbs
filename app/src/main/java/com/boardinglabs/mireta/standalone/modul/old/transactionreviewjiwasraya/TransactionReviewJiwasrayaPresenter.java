package com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya;

/**
 * Created by Dhimas on 12/8/17.
 */

public interface TransactionReviewJiwasrayaPresenter {
    void onRegisterPremium(String refferalId);

    void getBalance();

    void payTransaction(String transactionId, String billCode);

    void checkRefferal(String code);

    void calculate(String amount, String type, String merchantId);

//    void chargeTransaction(String transactionId);
}
