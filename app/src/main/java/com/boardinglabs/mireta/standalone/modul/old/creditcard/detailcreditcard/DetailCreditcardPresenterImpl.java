package com.boardinglabs.mireta.standalone.modul.old.creditcard.detailcreditcard;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CreditCardResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/16/18.
 */

public class DetailCreditcardPresenterImpl implements DetailCreditcardPresenter {
    private CommonInterface cInterface;
    private ActionDetailCreditcardListener mListener;

    public DetailCreditcardPresenterImpl(CommonInterface cInterface, ActionDetailCreditcardListener mListener ) {
        this.cInterface = cInterface;
        this.mListener = mListener;
    }

    @Override
    public void setDefault(String tokenId) {
        cInterface.showProgressLoading();
        setDefaultCard(tokenId).subscribe(new Subscriber<CreditCardResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(CreditCardResponse creditCardResponse) {
                cInterface.hideProgresLoading();
                mListener.onSuccessSetDefault();
            }
        });
    }

    public interface ActionDetailCreditcardListener{
        void onSuccessSetDefault();
    }

    Observable<CreditCardResponse> setDefaultCard(String tokenId){
        return cInterface.getService().setDefaultCard(tokenId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
