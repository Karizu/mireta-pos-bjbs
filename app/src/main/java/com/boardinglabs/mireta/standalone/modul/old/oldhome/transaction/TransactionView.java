package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;

import java.util.List;

/**
 * Created by Dhimas on 12/21/17.
 */

public interface TransactionView {
    void onSuccessGetTransaction(List<GTransaction> response);

    void onSuccessGetTopup(List<GTransactionTopup> response);

    void onSuccessGetCashback(List<GCashbackAgent> response);

    void hideProgressList();
}
