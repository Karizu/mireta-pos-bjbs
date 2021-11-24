package com.boardinglabs.mireta.standalone.modul.old.feed.chats;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLog;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLogGroup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransferRequestLogGroupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransferRequestLogResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import java.util.List;

import rx.Subscriber;

public class ChatPresenter {
    private CommonInterface cInterface;
    private ChatInteractor mInteractor;
    private ChatView chatView;

    public ChatPresenter(CommonInterface cInterface, ChatView listener) {
        this.cInterface = cInterface;
        mInteractor = new ChatInteractor(this.cInterface.getService());
        chatView = listener;
    }

    public void getLogGroups() {
        cInterface.showProgressLoading();
        mInteractor.getTransferRequestLogGroups().subscribe(new Subscriber<TransferRequestLogGroupResponse>() {
            @Override
            public void onCompleted() {
                cInterface.hideProgresLoading();
            }

            @Override
            public void onError(Throwable e) {
                //Log.i("kunam", ResponeError.getErrorMessage(e));
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                chatView.onFailureGetTransferRequestLogGroups(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransferRequestLogGroupResponse response) {
                cInterface.hideProgresLoading();
                if (response.success){
                    if (chatView != null) {
                        chatView.successGetTransferRequestLogGroups(response.log_groups);
                    }
                }
                else{
                    //Log.i("kunam", response.message);
                    cInterface.onFailureRequest(response.message);
                }

            }
        });
    }

    public void getLogs(String to_customer_id) {
//        cInterface.showProgressLoading();
        mInteractor.getTransferRequestLogs(to_customer_id).subscribe(new Subscriber<TransferRequestLogResponse>() {
            @Override
            public void onCompleted() {
                cInterface.hideProgresLoading();
            }

            @Override
            public void onError(Throwable e) {
//                cInterface.hideProgresLoading();
                //Log.i("kunam", ResponeError.getErrorMessage(e));
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                chatView.onFailureGetTransferRequestLogGroups(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransferRequestLogResponse response) {
//                cInterface.hideProgresLoading();
                if (response.success){
                    if (chatView != null) {
                        chatView.successGetTransferRequestLogs(response.logs, response.customer, response.to_customer);
                    }
                }
                else{
                    cInterface.onFailureRequest(response.message);
                    //Log.i("kunam", response.message);
                }

            }
        });
    }

    public interface ChatView {
        void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups);
        void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer);
        void onFailureGetTransferRequestLogGroups(String message);
    }
}
