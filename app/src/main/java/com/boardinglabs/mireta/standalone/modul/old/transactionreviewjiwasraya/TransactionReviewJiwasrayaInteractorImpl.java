package com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/8/17.
 */

public class TransactionReviewJiwasrayaInteractorImpl implements TransactionReviewJiwasrayaInteractor {
    private NetworkService mService;

    public TransactionReviewJiwasrayaInteractorImpl(NetworkService mService) {
        this.mService = mService;
    }

    @Override
    public Observable<MessageResponse> subscribePremium(String refferalId) {
        return mService.subscribePremium(refferalId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> payTransactionJiwasraya(String transactionId, String billCode) {
        return mService.payTransactionJiwasraya(transactionId, billCode).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }
}
