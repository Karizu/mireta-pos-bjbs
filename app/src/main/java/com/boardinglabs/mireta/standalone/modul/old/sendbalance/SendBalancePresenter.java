package com.boardinglabs.mireta.standalone.modul.old.sendbalance;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface SendBalancePresenter {
    void sendBalance(String mobile, String amount, String notes);
    void sendBalanceByRequest(String requestId);
}
