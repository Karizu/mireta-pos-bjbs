package com.boardinglabs.mireta.standalone.modul.old.register.otp;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/23/17.
 */

public class OtpPresenterImpl implements OtpPresenter {
    private CommonInterface cInterface;
    private OtpView mView;
    private OtpInteractor mInteractor;

    public OtpPresenterImpl(CommonInterface commonInterface, OtpView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new OtpInteractorImpl(cInterface.getService());
    }

    @Override
    public void verifyAgent(String code, String mobile) {
        cInterface.showProgressLoading();
        mInteractor.verifyAgent(mobile, code).subscribe(new Subscriber<AgentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(AgentResponse agentResponse) {
                cInterface.hideProgresLoading();
                PreferenceManager.setSessionToken(agentResponse.customers.access_token);
                PreferenceManager.setAgent(agentResponse.customers);
                mView.onSuccessRequest();
            }
        });
    }

    @Override
    public void resendCode(String mobile) {
        cInterface.showProgressLoading();
        mInteractor.resendCode(mobile).subscribe(new Subscriber<MessageResponse>() {
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
                mView.onSuccessResendCode(messageResponse.message);
            }
        });
    }
}
