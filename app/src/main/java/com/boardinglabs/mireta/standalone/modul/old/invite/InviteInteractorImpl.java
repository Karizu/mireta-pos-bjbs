package com.boardinglabs.mireta.standalone.modul.old.invite;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InviteInteractorImpl implements InviteInteractor{
    private NetworkService mService;

    public InviteInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> invite(String mobiles) {
        return mService.invite(mobiles).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
