package com.boardinglabs.mireta.standalone.modul.old.register.passcode;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/23/17.
 */

public class PasscodePresenterImpl implements PasscodePresenter {
    private PasscodeView mView;
    private CommonInterface cInterface;
    private PasscodeInteractor mInteractor;

    public PasscodePresenterImpl(CommonInterface commonInterface, PasscodeView view) {
        cInterface = commonInterface;
        mView = view;
        mInteractor = new PasscodeInteractorImpl(cInterface.getService());
    }

    @Override
    public void setPasscode(String passcode, String confirmPasscode) {
        cInterface.showProgressLoading();
        mInteractor.setPasscode(passcode, confirmPasscode).subscribe(new Subscriber<MessageResponse>() {
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
                GAgent agent = PreferenceManager.getAgent();
                PreferenceManager.logIn(agent.access_token, agent.name, agent.mobile, agent.whitelist_topup);
                cInterface.hideProgresLoading();
                mView.onSuccessResponse();
            }
        });
    }
}
