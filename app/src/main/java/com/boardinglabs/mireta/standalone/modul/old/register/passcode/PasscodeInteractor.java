package com.boardinglabs.mireta.standalone.modul.old.register.passcode;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface PasscodeInteractor {
    Observable<MessageResponse> setPasscode(String passcode, String confPasscode);
}
