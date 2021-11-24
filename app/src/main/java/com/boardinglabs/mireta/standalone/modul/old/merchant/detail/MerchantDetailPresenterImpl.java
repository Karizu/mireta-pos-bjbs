package com.boardinglabs.mireta.standalone.modul.old.merchant.detail;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantDetailPresenterImpl implements MerchantDetailPresenter {
    private CommonInterface commonInterface;
    private MerchantDetailView mView;
    private Merchant merchant;

    public MerchantDetailPresenterImpl(CommonInterface cInterface, MerchantDetailView view){
        commonInterface = cInterface;
        mView = view;
    }

    @Override
    public void getDetail(String merchantCode) {
        commonInterface.showProgressLoading();
        getMerchantDetail(merchantCode).subscribe(new Subscriber<GMerchant>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                commonInterface.hideProgresLoading();
                commonInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GMerchant gMerchant) {
                commonInterface.hideProgresLoading();
                mView.onGetMerchantDetail(gMerchant);
            }
        });
    }

    Observable<GMerchant> getMerchantDetail(String code){
        return commonInterface.getService().getMerchantDetail(code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
