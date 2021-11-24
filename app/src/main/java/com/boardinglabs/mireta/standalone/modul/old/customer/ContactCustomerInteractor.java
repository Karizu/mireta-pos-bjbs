package com.boardinglabs.mireta.standalone.modul.old.customer;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.SyncContactResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactCustomerInteractor {
    private NetworkService mService;

    public ContactCustomerInteractor(NetworkService mService) {
        this.mService = mService;
    }

    public Observable<SyncContactResponse> syncContacts(String mobiles) {
        return mService.syncContact(mobiles).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
