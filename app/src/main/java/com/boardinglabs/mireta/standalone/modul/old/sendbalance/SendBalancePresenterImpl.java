package com.boardinglabs.mireta.standalone.modul.old.sendbalance;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/29/17.
 */

public class SendBalancePresenterImpl implements SendBalancePresenter {
    private CommonInterface cInterface;
    private SendBalanceView mView;
    private SendBalanceInteractor mInteractor;

    public SendBalancePresenterImpl(CommonInterface commonInterface, SendBalanceView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new SendBalanceInteractorImpl(cInterface.getService());
    }

    @Override
    public void sendBalance(String mobile, String amount, String notes) {
        cInterface.showProgressLoading();
        mInteractor.transferBalance(mobile, amount, notes).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessSendBalance();
            }
        });
    }

    @Override
    public void sendBalanceByRequest(String requestId) {
        cInterface.showProgressLoading();
        mInteractor.transferBalanceByRequest(requestId).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessSendBalance();
            }
        });
    }
}
