package com.boardinglabs.mireta.standalone.modul;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;

/**
 * Created by Dhimas on 11/23/17.
 */

public interface CommonInterface {
    void showProgressLoading();

    void hideProgresLoading();

    NetworkService getService();

    void onFailureRequest(String msg);
}
