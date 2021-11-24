package com.boardinglabs.mireta.standalone.modul.old.promo;

import com.boardinglabs.mireta.standalone.component.network.gson.GPromo;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PromoResponse;

import rx.Observable;

/**
 * Created by Dhimas on 2/18/18.
 */

public interface PromoInteractor {
    Observable<PromoResponse> getList();

    Observable<GPromo> getPromoDetail(String promoId);
}
