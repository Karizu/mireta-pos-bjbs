package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GMerchant;
import com.boardinglabs.mireta.standalone.component.network.gson.GPagination;

import java.util.List;

/**
 * Created by Dhimas on 2/6/18.
 */

public class MerchantResponse {
    public List<GMerchant> items;

    public GPagination pagination;
}
