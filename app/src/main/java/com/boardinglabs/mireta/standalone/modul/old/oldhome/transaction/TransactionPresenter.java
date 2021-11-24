package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

/**
 * Created by Dhimas on 12/21/17.
 */

public interface TransactionPresenter {
    void getTransaction();

    void getTransactionPPOB();

    void loadNextTransaction();

    void loadNextTransactionPPOB();

    void getTopup();

    void loadNextTopup();

    void getCashback();

    void loadNextCashback();

    void updateBank(String orderId, String bankId, String accountId);
}
