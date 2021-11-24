package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GCashbackAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransactionTopup;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.BankTransferResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CashbackResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TopupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionHistoryResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Dhimas on 12/21/17.
 */

public class TransactionPresenterImpl implements TransactionPresenter {
    private CommonInterface cInterface;
    private TransactionView mView;
    private TransactionInteractor mInteractor;
    private int pageTransaction;
    private int pageTopup;
    private int pageCashback;

    public TransactionPresenterImpl(CommonInterface commonInterface, TransactionView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new TransactionInteractorImpl(cInterface.getService());
        pageTransaction = 1;
        pageCashback = 1;
        pageTopup = 1;
    }

    @Override
    public void getTransaction() {
        cInterface.showProgressLoading();
        mInteractor.getTransaction(pageTransaction).subscribe(new Subscriber<TransactionHistoryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransactionHistoryResponse response) {
                cInterface.hideProgresLoading();
                if (response.pagination.has_next) {
                    pageTransaction++;
                    response.items.add(null);
                }
                mView.onSuccessGetTransaction(response.items);
            }
        });
    }

    @Override
    public void getTransactionPPOB() {
        cInterface.showProgressLoading();
        mInteractor.getTransactionPpob(pageTransaction).subscribe(new Subscriber<TransactionHistoryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransactionHistoryResponse response) {
                cInterface.hideProgresLoading();
                if (response.pagination.has_next) {
                    pageTransaction++;
                    response.items.add(null);
                }
//                Log.i("kunam", response.transactionItems.get(1).status);
//                Log.i("kunam", response.transactionItems.get(1).status_label);
                mView.onSuccessGetTransaction(response.items);
            }
        });
    }

    @Override
    public void loadNextTransaction() {
        mInteractor.getTransaction(pageTransaction).subscribe(new Subscriber<TransactionHistoryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransactionHistoryResponse response) {
                mView.hideProgressList();
                if (response.pagination.has_next) {
                    pageTransaction++;
                    response.items.add(null);
                }
                mView.hideProgressList();
                mView.onSuccessGetTransaction(response.items);
            }
        });
    }

    @Override
    public void loadNextTransactionPPOB() {
        mInteractor.getTransactionPpob(pageTransaction).subscribe(new Subscriber<TransactionHistoryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransactionHistoryResponse response) {
                mView.hideProgressList();
                if (response.pagination.has_next) {
                    pageTransaction++;
                    response.items.add(null);
                }
                mView.hideProgressList();
                mView.onSuccessGetTransaction(response.items);
            }
        });
    }

    @Override
    public void getTopup() {
        mInteractor.getTopup(pageTopup).subscribe(new Subscriber<TopupResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TopupResponse response) {
                List<GTransactionTopup> topupList = new ArrayList<>();
                topupList.addAll(response.items);
                if (response.pagination.has_next) {
                    topupList.add(null);
                    //pageTopup++;
                }
                mView.onSuccessGetTopup(topupList);
            }
        });
    }

    @Override
    public void loadNextTopup() {
        mInteractor.getTopup(pageTopup).subscribe(new Subscriber<TopupResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TopupResponse response) {
                mView.hideProgressList();
                List<GTransactionTopup> topupList = new ArrayList<>();
                topupList.addAll(response.items);
                if (response.pagination.has_next) {
                    topupList.add(null);
                    //pageTopup++;
                }
                mView.onSuccessGetTopup(topupList);
            }
        });
    }

    @Override
    public void getCashback() {
        mInteractor.getCashback(pageCashback).subscribe(new Subscriber<CashbackResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(CashbackResponse response) {
                List<GCashbackAgent> cashbackAgents = removeItemRedeem(response.items);
                if (response.pagination.has_next) {
                    cashbackAgents.add(null);
                    pageCashback++;
                }
                mView.onSuccessGetCashback(cashbackAgents);
            }
        });
    }

    private List<GCashbackAgent> removeItemRedeem(List<GCashbackAgent> cashback) {
        List<GCashbackAgent> cashbackAgents = new ArrayList<>();

        for (GCashbackAgent cashbackAgent : cashback) {
            if (cashbackAgent.status != Constant.TYPE_REDEEM) {
                cashbackAgents.add(cashbackAgent);
            }
        }

        return cashbackAgents;
    }

    @Override
    public void loadNextCashback() {
        mInteractor.getCashback(pageCashback).subscribe(new Subscriber<CashbackResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(CashbackResponse response) {
                List<GCashbackAgent> cashbackAgents = removeItemRedeem(response.items);
                mView.hideProgressList();
                if (response.pagination.has_next) {
                    cashbackAgents.add(null);
                    pageCashback++;
                }
                mView.onSuccessGetCashback(cashbackAgents);
            }
        });
    }

    @Override
    public void updateBank(String orderId, String bankId, String accountId) {
        cInterface.showProgressLoading();
        mInteractor.updateBank(orderId, bankId, accountId).subscribe(new Subscriber<BankTransferResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(BankTransferResponse bankTransferResponse) {
                cInterface.hideProgresLoading();
            }
        });
    }
}
