package com.boardinglabs.mireta.standalone.modul.old.register;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;

/**
 * Created by Dhimas on 11/22/17.
 */

public interface RegisterView {
    NetworkService getService();

    void onSuccessRegister(String mobile);

    void onFailedRegister(String msg);

    void onSuccessResendCode(String msg);

    void onSuccessCheckReferral(String refferalId);

    void showProgressBar();

    void hideProgressBar();
}
