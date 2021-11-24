package com.boardinglabs.mireta.standalone.modul.old.topup.topup;

import com.boardinglabs.mireta.standalone.component.network.gson.GTopup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.VoucherResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface TopupInteractor {
    Observable<VoucherResponse> checkVoucher(String code, String amount);

    Observable<GTopup> topup(String amount, String voucherId, String method);
}
