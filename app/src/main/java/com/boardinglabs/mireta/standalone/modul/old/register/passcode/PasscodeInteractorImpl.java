package com.boardinglabs.mireta.standalone.modul.old.register.passcode;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/23/17.
 */

public class PasscodeInteractorImpl implements PasscodeInteractor {
    NetworkService mNetworkService;

    public PasscodeInteractorImpl(NetworkService service) {
        mNetworkService = service;
    }
    @Override
    public Observable<MessageResponse> setPasscode(String passcode, String confPasscode) {
        return mNetworkService.setPasscode(passcode, confPasscode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
