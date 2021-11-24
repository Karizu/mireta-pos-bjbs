package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLogGroup;

import java.util.List;

public class TransferRequestLogGroupResponse {

    public boolean success;
    public String message;
    public List<GTransferRequestLogGroup> log_groups;
}
