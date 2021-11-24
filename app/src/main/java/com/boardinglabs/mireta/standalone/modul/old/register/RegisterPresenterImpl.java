package com.boardinglabs.mireta.standalone.modul.old.register;

import android.text.TextUtils;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpInteractor;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpInteractorImpl;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/22/17.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    private RegisterInteractor mInteractor;
    private OtpInteractor oInteractor;
    private RegisterView mView;

    public RegisterPresenterImpl(RegisterView view) {
        mView = view;
        mInteractor = new RegisterInteractorImpl(view.getService());
        oInteractor = new OtpInteractorImpl(view.getService());
    }
    @Override
    public void register(String name, String mobile, String email, String refferalId) {
        if (isValidData(name, mobile, email)) {
            mView.showProgressBar();
            mInteractor.register(name, email, mobile, refferalId).subscribe(new Subscriber<MessageResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.hideProgressBar();
                    mView.onFailedRegister(ResponeError.getErrorMessage(e));
                }

                @Override
                public void onNext(MessageResponse msg) {
                    mView.hideProgressBar();
                    mView.onSuccessRegister(msg.message);
                }
            });
        }
    }

    @Override
    public void sendCode(String mobile) {
        mView.showProgressBar();
        oInteractor.resendCode(mobile).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgressBar();
                mView.onFailedRegister(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                mView.hideProgressBar();
                mView.onSuccessResendCode(messageResponse.message);
            }
        });
    }

    @Override
    public void checkRefferalId(String code) {
        mView.showProgressBar();
        mInteractor.checkRefferal(code).subscribe(new Subscriber<AgentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgressBar();
                mView.onFailedRegister(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(AgentResponse agentResponse) {
                mView.hideProgressBar();
                mView.onSuccessCheckReferral(agentResponse.customers.id);
            }
        });
    }

    private Boolean isValidData(String name, String mobile, String email) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(email)) {
            mView.onFailedRegister("Harap isi kolom yang masih kosong");
            return false;
        }
        return true;
    }
}
