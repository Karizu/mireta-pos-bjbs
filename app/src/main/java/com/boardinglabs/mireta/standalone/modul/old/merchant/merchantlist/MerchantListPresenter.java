package com.boardinglabs.mireta.standalone.modul.old.merchant.merchantlist;

import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;

import java.util.List;

/**
 * Created by Dhimas on 2/6/18.
 */

public interface MerchantListPresenter {
    void getMerchantList(String query);

    void loadNextTransactionPage();

    List<GMerchant> getMerchant();
}
