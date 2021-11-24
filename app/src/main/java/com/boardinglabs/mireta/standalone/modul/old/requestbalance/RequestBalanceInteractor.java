package com.boardinglabs.mireta.standalone.modul.old.requestbalance;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface RequestBalanceInteractor {
    Observable<MessageResponse> requestBalance(String destCustId, String amount, String notes);
    Observable<MessageResponse> rejectRequest(String requestId);
}
