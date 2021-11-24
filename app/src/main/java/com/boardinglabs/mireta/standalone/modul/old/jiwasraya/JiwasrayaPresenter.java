package com.boardinglabs.mireta.standalone.modul.old.jiwasraya;

/**
 * Created by Dhimas on 12/23/17.
 */

public interface JiwasrayaPresenter {
//    void updateProfile(String name, String mobile, String email);
    void setInquiry(String serviceId, String customerNo, boolean usingInquiry, String amount);
    void updateAmountInquiry(String transactionId, String amount);
}
