package com.boardinglabs.mireta.standalone.modul.old.sendbalance;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface SendBalanceInteractor {
    Observable<MessageResponse> transferBalance(String mobile, String amount, String notes);
    Observable<MessageResponse> transferBalanceByRequest(String requestId);
}
