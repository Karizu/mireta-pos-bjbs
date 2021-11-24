package com.boardinglabs.mireta.standalone.modul.old.feed.chats;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransferRequestLogGroupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransferRequestLogResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatInteractor {
    private NetworkService mService;

    public ChatInteractor(NetworkService mService) {
        this.mService = mService;
    }

    public Observable<TransferRequestLogGroupResponse> getTransferRequestLogGroups() {
        return mService.getTransferRequestLogGroup().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<TransferRequestLogResponse> getTransferRequestLogs(String to_customer_id) {
        return mService.getTransferRequestLog(to_customer_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
