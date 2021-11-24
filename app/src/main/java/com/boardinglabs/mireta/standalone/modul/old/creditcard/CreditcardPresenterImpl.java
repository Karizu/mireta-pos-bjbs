package com.boardinglabs.mireta.standalone.modul.old.creditcard;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CalculateResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CreditCardResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.inputcreditcard.InputCreditcardView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/2/18.
 */

public class CreditcardPresenterImpl implements CreditcardPresenter {
    private CreditcardInteractor mInteractor;
    private CommonInterface cInterface;
    private InputCreditcardView mView;

    public CreditcardPresenterImpl(CommonInterface cInterface, InputCreditcardView mView) {
        mInteractor = new CreditcardInteractorImpl(cInterface.getService());
        this.cInterface = cInterface;
        this.mView = mView;
    }

    @Override
    public void onTransaction(String charge, String totalAmount, String note, boolean activeImage,
                              String amount, String voucherId, String voucherAmount) {

    }

    @Override
    public void onFastTransaction(String charge, String totalAmount, String note, boolean activeImage,
                                  String amount, String voucherId, String voucherAmount, String name,
                                  String email, String phone, String merhcnatId, String qrId, String cardHash, String cardType) {
        cInterface.showProgressLoading();
        mInteractor.fastTransactionMidtrans(merhcnatId, cardType, totalAmount, "", note, null, voucherId, name, email, phone, "", "").subscribe(new Subscriber<GCreditCard>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GCreditCard gCreditCard) {
                cInterface.hideProgresLoading();
                mView.onSuccessTransactionMidtrans(gCreditCard);
            }
        });
    }

    @Override
    public void addCreditcard(final String tokenId, String cardhash, String cardName, String expiryMonth, String expiryYear, String expiredToken) {
        cInterface.showProgressLoading();
        mInteractor.addCreditCard(tokenId, cardhash, cardName, expiryMonth, expiryYear, expiredToken).subscribe(new Subscriber<JsonObject>() {
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
//                mView.onSuccessTransaction();
                setDefault(tokenId);
            }
        });
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
//                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                mView.onSuccessTransaction();
            }

            @Override
            public void onNext(CreditCardResponse creditCardResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessTransaction();
            }
        });
    }

    @Override
    public void calculateAmount(String amount, String type, String merchantId) {
        cInterface.showProgressLoading();
        calculate(amount, type, merchantId).subscribe(new Subscriber<CalculateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(CalculateResponse calculateResponse) {
                cInterface.hideProgresLoading();
                mView.setTotalAmount(calculateResponse.charge);
            }
        });
    }

    Observable<CalculateResponse> calculate(String amount, String type, String merchantId) {
        return cInterface.getService().calculateAmount(amount, type, merchantId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    Observable<CreditCardResponse> setDefaultCard(String tokenId){
        return cInterface.getService().setDefaultCard(tokenId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
