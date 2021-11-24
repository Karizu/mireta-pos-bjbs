package com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GBanks;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.BankTransferResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/28/17.
 */

public class PaymentMethodTopupPresenterImpl implements PaymentMethodTopupPresenter {
    private CommonInterface cInterface;
    private PaymentMethodTopupView mView;
    private PaymentMethodTopupInteractorImpl mInteractor;

    public PaymentMethodTopupPresenterImpl(CommonInterface commonInterface, PaymentMethodTopupView view) {
        cInterface = commonInterface;
        mView = view;
        mInteractor = new PaymentMethodTopupInteractorImpl(cInterface.getService());
    }

    @Override
    public void requestBanks() {
        cInterface.showProgressLoading();
        mInteractor.getBank().subscribe(new Subscriber<List<GBanks>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                cInterface.hideProgresLoading();
            }

            @Override
            public void onNext(List<GBanks> banks) {
                cInterface.hideProgresLoading();
                mView.onSuccessRequestBanks(banks);
            }
        });
    }

    @Override
    public void confirmPayment(String orderId, String bankId, String accountid) {
        cInterface.showProgressLoading();
        mInteractor.topupConfirm(orderId, bankId, accountid).subscribe(new Subscriber<BankTransferResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(BankTransferResponse bankTransferResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessRequest(bankTransferResponse.top_up, bankTransferResponse.customer_avatar, bankTransferResponse.customer_mobile);
            }
        });
    }
}
