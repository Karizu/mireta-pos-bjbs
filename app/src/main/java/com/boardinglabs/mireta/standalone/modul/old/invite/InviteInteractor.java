package com.boardinglabs.mireta.standalone.modul.old.invite;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;

import rx.Observable;

public interface InviteInteractor {
    Observable<MessageResponse> invite(String mobiles);
}
