package com.boardinglabs.mireta.standalone.modul.old.oldhome;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GBalance;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/27/17.
 */

public class HomeInteractorImpl implements HomeInteractor {
    private NetworkService mService;

    public HomeInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<GBalance> getBalance() {
        return mService.getBalance().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<JsonObject> checkPremiumAgent() {
        return mService.checkPremium().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
