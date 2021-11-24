package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GLogo;
import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;
import com.boardinglabs.mireta.standalone.component.network.gson.GVoucher;

/**
 * Created by Dhimas on 2/1/18.
 */

public class QRResponse {
    public String id;
    public String merchant_id;
    public String amount;
    public String notes;
    public GLogo image;
    public String code;
    public GMerchant merchant;
    public String is_used;
    public GVoucher voucher;
    public String voucher_id;
    public String type;
}
