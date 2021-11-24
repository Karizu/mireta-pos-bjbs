package com.boardinglabs.mireta.standalone.modul.transactions.items;

import com.boardinglabs.mireta.standalone.component.network.entities.Item;

import java.util.List;

import okhttp3.ResponseBody;

public interface ItemsView {
    void onSuccessGetItems(List<Item> transactionItems);
    void onSuccessCreateTransaction(ResponseBody responseBody);
}
