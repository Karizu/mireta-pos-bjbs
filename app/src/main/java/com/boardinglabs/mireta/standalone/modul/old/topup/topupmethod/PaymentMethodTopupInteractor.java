package com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod;

import com.boardinglabs.mireta.standalone.component.network.gson.GBanks;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.BankTransferResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 11/28/17.
 */

public interface PaymentMethodTopupInteractor {
    Observable<List<GBanks>> getBank();

    Observable<BankTransferResponse> topupConfirm(String orderId, String bankId, String accountId);

}
