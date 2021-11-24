package com.boardinglabs.mireta.standalone.modul.old.promo;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.DealsResponse;

import rx.Observable;

/**
 * Created by Dhimas on 4/26/18.
 */

public interface DealsInteractor {
    Observable<DealsResponse> getDeals();

    Observable<JsonObject> redeemDeal(String dealID);
}
