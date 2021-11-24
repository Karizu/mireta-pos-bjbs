package com.boardinglabs.mireta.standalone.modul.old.oldhome;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.gson.GBalance;

import rx.Observable;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface HomeInteractor {
    Observable<GBalance> getBalance();
    Observable<JsonObject> checkPremiumAgent();
}
