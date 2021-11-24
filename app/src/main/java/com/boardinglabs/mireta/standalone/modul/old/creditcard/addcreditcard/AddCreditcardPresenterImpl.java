package com.boardinglabs.mireta.standalone.modul.old.creditcard.addcreditcard;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CreditCardResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardInteractor;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.CreditcardInteractorImpl;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/18/18.
 */

public class AddCreditcardPresenterImpl implements AddCreditcardPresenter {
    private CreditcardInteractor mInteractor;
    private CommonInterface cInterface;
    private EventAddCreditcardPresenter mListener;
    private boolean isAlreadyHaveCC;

    public AddCreditcardPresenterImpl(CommonInterface cInterface, EventAddCreditcardPresenter listener, boolean isHaveCreditcard) {
        mInteractor = new CreditcardInteractorImpl(cInterface.getService());
        this.cInterface = cInterface;
        mListener = listener;
        isAlreadyHaveCC = isHaveCreditcard;
    }

    @Override
    public void addCreditcard(String cardhash, final String savedTokenId, String expiryMonth, String expiryYear) {
        cInterface.showProgressLoading();
        mInteractor.addCreditCard(savedTokenId, cardhash, "", expiryMonth, expiryYear, "").subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                cInterface.hideProgresLoading();
                if (mListener != null) {
                    if (isAlreadyHaveCC) {
                        mListener.onSuccessAddCreditcard();
                    } else {
                        setDefault(savedTokenId);
                    }

                }
            }
        });
    }

    private void setDefault(String token) {
        cInterface.showProgressLoading();
        setDefaultCard(token).subscribe(new Subscriber<CreditCardResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                afterSetDefaultCard();
            }

            @Override
            public void onNext(CreditCardResponse creditCardResponse) {
                afterSetDefaultCard();
            }
        });
    }

    private void afterSetDefaultCard() {
        cInterface.hideProgresLoading();
        if (mListener != null) {
            mListener.onSuccessAddCreditcard();
        }
    }

    public interface EventAddCreditcardPresenter{
        void onSuccessAddCreditcard();
    }

    Observable<CreditCardResponse> setDefaultCard(String tokenId){
        return cInterface.getService().setDefaultCard(tokenId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
