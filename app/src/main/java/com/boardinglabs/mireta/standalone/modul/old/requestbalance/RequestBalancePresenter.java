package com.boardinglabs.mireta.standalone.modul.old.requestbalance;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface RequestBalancePresenter {
    void requestBalance(String destCustId, String amount, String notes);
    void rejectRequest(String requestId);
}
