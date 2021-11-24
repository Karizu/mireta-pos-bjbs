package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GTransaction;

/**
 * Created by Dhimas on 2/1/18.
 */

public class QRTransactionResponse {
    public boolean success;
    public QRResponse item;
    public String message;
    public GTransaction transaction;
}
