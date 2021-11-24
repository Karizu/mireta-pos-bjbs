package com.boardinglabs.mireta.standalone.modul.old.topup.topup;

import com.boardinglabs.mireta.standalone.component.network.gson.GTopup;
import com.boardinglabs.mireta.standalone.component.network.gson.GVoucher;

/**
 * Created by Dhimas on 11/27/17.
 */

public interface TopupView {
    void onSuccessRequest(GTopup topup);

    void onSuccessRequestVoucher(GVoucher voucher);
}
