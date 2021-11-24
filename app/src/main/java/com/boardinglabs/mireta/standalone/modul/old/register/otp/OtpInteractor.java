package com.boardinglabs.mireta.standalone.modul.old.register.otp;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface OtpInteractor {
    Observable<AgentResponse> verifyAgent(String mobile, String code);

    Observable<MessageResponse> resendCode(String mobile);
}
