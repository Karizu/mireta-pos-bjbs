package com.boardinglabs.mireta.standalone.modul.old.invite;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

public class InvitePresenterImpl implements InvitePresenter {
    private CommonInterface cInterface;
    private InviteView mView;
    private InviteInteractor mInteractore;

    public InvitePresenterImpl(CommonInterface cInterface, InviteView view) {
        this.cInterface = cInterface;
        mView = view;
        mInteractore = new InviteInteractorImpl(cInterface.getService());
    }

    @Override
    public void sentInvite(String mobiles) {
        cInterface.showProgressLoading();
        mInteractore.invite(mobiles).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {
                cInterface.hideProgresLoading();
            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse response) {
                cInterface.hideProgresLoading();
                mView.onSuccessInvite(response.message);
            }
        });
    }
}
