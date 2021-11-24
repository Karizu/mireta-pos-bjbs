package com.boardinglabs.mireta.standalone.modul.old.register;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/22/17.
 */

public class RegisterInteractorImpl implements RegisterInteractor {
    private NetworkService mService;

    public RegisterInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<MessageResponse> register(String name, String email, String mobile, String refferalId) {
        return mService.register(name, mobile, email, refferalId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<AgentResponse> checkRefferal(String code) {
        return mService.checkRefferalCode(code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
