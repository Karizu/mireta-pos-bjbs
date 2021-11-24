package com.boardinglabs.mireta.standalone.modul.old.requestbalance;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/29/17.
 */

public class RequestBalancePresenterImpl implements RequestBalancePresenter {
    private CommonInterface cInterface;
    private RequestBalanceView mView;
    private RequestBalanceInteractor mInteractor;

    public RequestBalancePresenterImpl(CommonInterface commonInterface, RequestBalanceView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new RequestBalanceInteractorImpl(cInterface.getService());
    }

    @Override
    public void requestBalance(String destCustId, String amount, String notes) {
        cInterface.showProgressLoading();
        mInteractor.requestBalance(destCustId, amount, notes).subscribe(new Subscriber<MessageResponse>() {
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
                mView.onSuccessRequestBalance();
            }
        });
    }

    @Override
    public void rejectRequest(String requestId) {
        cInterface.showProgressLoading();
        mInteractor.rejectRequest(requestId).subscribe(new Subscriber<MessageResponse>() {
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
                mView.onSuccessRequestBalance();
            }
        });
    }
}
