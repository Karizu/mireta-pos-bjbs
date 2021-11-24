package com.boardinglabs.mireta.standalone.modul.old.topup.topupmethod;

import com.boardinglabs.mireta.standalone.component.network.gson.GBanks;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TopupResponse;

import java.util.List;

/**
 * Created by Dhimas on 11/28/17.
 */

public interface PaymentMethodTopupView {
    void onSuccessRequest(TopupResponse response, String avatar, String mobile);

    void onSuccessRequestBanks(List<GBanks> listBank);
}
