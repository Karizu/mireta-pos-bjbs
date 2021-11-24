package com.boardinglabs.mireta.standalone.modul.old.register;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/22/17.
 */

public interface RegisterInteractor {
    Observable<MessageResponse> register(String name, String email, String mobile, String refferalId);

    Observable<AgentResponse> checkRefferal(String code);
}
