package com.boardinglabs.mireta.standalone.modul.old.transactionreview;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/8/17.
 */

public interface TransactionReviewInteractor {
    Observable<MessageResponse> subscribePremium(String refferalId);

    Observable<TransactionResponse> payTransaction(String transactionId);
}
