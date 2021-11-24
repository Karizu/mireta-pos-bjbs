package com.boardinglabs.mireta.standalone.modul.old.scanqr;

/**
 * Created by Dhimas on 2/2/18.
 */

public interface ScanQRView {
    void openTransactionActivity(String merchantId, String amount,
                                 String notes, String orderId, String merchantName, String voucherid, String type);
}
