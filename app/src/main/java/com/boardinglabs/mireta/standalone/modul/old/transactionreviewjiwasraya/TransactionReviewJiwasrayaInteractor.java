package com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/8/17.
 */

public interface TransactionReviewJiwasrayaInteractor {
    Observable<MessageResponse> subscribePremium(String refferalId);

    Observable<TransactionResponse> payTransactionJiwasraya(String transactionId,String billCode);
}
