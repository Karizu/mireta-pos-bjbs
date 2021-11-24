package com.boardinglabs.mireta.standalone.modul.old.purchase;

import com.boardinglabs.mireta.standalone.component.network.gson.GServices;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;

import java.util.List;

/**
 * Created by Dhimas on 12/14/17.
 */

public interface PurchaseView {
    void onSuccessGetService(List<GServices> listServices);

    void onSuccessInquiry(GTransaction transaction);
}
