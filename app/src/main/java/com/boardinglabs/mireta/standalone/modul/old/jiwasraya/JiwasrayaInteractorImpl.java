package com.boardinglabs.mireta.standalone.modul.old.jiwasraya;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.ServicesResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/14/17.
 */

public class JiwasrayaInteractorImpl implements JiwasrayaInteractor {
    private NetworkService mService;

    public JiwasrayaInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<ServicesResponse> getServices(String type, String amount, String no, String cat) {
        return mService.getService(type, amount, no, cat).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> setInquiry(String serviceId, String customerNo, String amount) {
        return mService.setInquiry(customerNo, serviceId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> getTransaction(String serviceId, String customerNo) {
        return mService.getTransactionWithoutInquiry(customerNo, serviceId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<TransactionResponse> updateAmountInquiry(String transactionId, String amount) {
        return  mService.updateAmountInquiry(transactionId, amount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
