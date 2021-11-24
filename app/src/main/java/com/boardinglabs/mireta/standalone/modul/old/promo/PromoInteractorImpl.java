package com.boardinglabs.mireta.standalone.modul.old.promo;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GPromo;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PromoResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/18/18.
 */

public class PromoInteractorImpl implements PromoInteractor {
    private NetworkService mService;

    public PromoInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<PromoResponse> getList() {
        return mService.getPromo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GPromo> getPromoDetail(String promoId) {
        return mService.getPromoDetail(promoId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
