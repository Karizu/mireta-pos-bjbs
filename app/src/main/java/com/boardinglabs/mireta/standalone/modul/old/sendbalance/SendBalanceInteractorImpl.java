package com.boardinglabs.mireta.standalone.modul.old.sendbalance;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/29/17.
 */

public class SendBalanceInteractorImpl implements SendBalanceInteractor {
    private NetworkService mService;

    public SendBalanceInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> transferBalance(String mobile, String amount, String notes) {
        return mService.transferBalance(mobile, amount, notes).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MessageResponse> transferBalanceByRequest(String requestId) {
        return mService.transferBalanceByRequest(requestId, "1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
