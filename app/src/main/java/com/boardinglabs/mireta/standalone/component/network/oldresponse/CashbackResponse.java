package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GPagination;

import java.util.List;

/**
 * Created by Dhimas on 12/26/17.
 */

public class CashbackResponse {
    public List<GCashbackAgent> items;
    public GPagination pagination;
}
