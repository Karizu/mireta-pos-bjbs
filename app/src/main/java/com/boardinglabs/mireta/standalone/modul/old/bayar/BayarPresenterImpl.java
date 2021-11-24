package com.boardinglabs.mireta.standalone.modul.old.bayar;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GLogo;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/6/18.
 */

public class BayarPresenterImpl implements BayarPresenter {

    private  CommonInterface cInterface;
    private BayarView mview;

    public BayarPresenterImpl(CommonInterface cInterface, BayarView view) {
        mview = view;
        this.cInterface = cInterface;
    }

    @Override
    public void getQrcode() {
        cInterface.showProgressLoading();
        getQR().subscribe(new Subscriber<GLogo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GLogo logo) {
                cInterface.hideProgresLoading();
                String url = logo.base_url + "/" + logo.path;
                mview.showQrcode(url);
            }
        });
    }

    Observable<GLogo> getQR (){
        return cInterface.getService().getQr().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
