package com.boardinglabs.mireta.standalone.modul.old.transactionreviewjiwasraya;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GBalance;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.AgentResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CalculateResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.QRTransactionResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomeInteractor;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomeInteractorImpl;
import com.boardinglabs.mireta.standalone.modul.old.register.RegisterInteractor;
import com.boardinglabs.mireta.standalone.modul.old.register.RegisterInteractorImpl;

import org.json.JSONException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/8/17.
 */

public class TransactionReviewJiwasrayaPresenterImpl implements TransactionReviewJiwasrayaPresenter {
    CommonInterface cInterface;
    TransactionReviewJiwasrayaView mView;
    TransactionReviewJiwasrayaInteractor mInteractor;
    HomeInteractor hInteractor;
    RegisterInteractor rInteractor;

    public TransactionReviewJiwasrayaPresenterImpl(CommonInterface cInterface, TransactionReviewJiwasrayaView view) {
        this.cInterface = cInterface;
        mView = view;
        mInteractor = new TransactionReviewJiwasrayaInteractorImpl(this.cInterface.getService());
        hInteractor = new HomeInteractorImpl(this.cInterface.getService());
        rInteractor = new RegisterInteractorImpl(this.cInterface.getService());
    }

    @Override
    public void onRegisterPremium(String refferalId) {
        cInterface.showProgressLoading();
        mInteractor.subscribePremium(refferalId).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessRegisterPremium(messageResponse);
            }
        });
    }

    @Override
    public void getBalance() {
        cInterface.showProgressLoading();
        hInteractor.getBalance().subscribe(new Subscriber<GBalance>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                //Log.i("kunam", ResponeError.getErrorMessage(e));
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GBalance gBalance) {
                cInterface.hideProgresLoading();
                mView.onSuccessGetBalance(gBalance.balance);
            }
        });
    }

    @Override
    public void payTransaction(String transactionId, String billCode) {
        cInterface.showProgressLoading();
        mInteractor.payTransactionJiwasraya(transactionId, billCode).subscribe(new Subscriber<TransactionResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(TransactionResponse transactionResponse) {
                cInterface.hideProgresLoading();
                try {
                    mView.onSuccessPayTransactionJiwasraya(transactionResponse.transactions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void checkRefferal(String code) {
        cInterface.showProgressLoading();
        rInteractor.checkRefferal(code).subscribe(new Subscriber<AgentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(AgentResponse agentResponse) {
                cInterface.hideProgresLoading();
                mView.onSuccessCheckReferral(agentResponse.customers.id);
            }
        });
    }

    @Override
    public void calculate(String amount, String type, String merchantId) {
        cInterface.showProgressLoading();
        calculateAmount(amount, type, merchantId).subscribe(new Subscriber<CalculateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(CalculateResponse calculateResponse) {
                cInterface.hideProgresLoading();
                int fee = Integer.parseInt(calculateResponse.charge) - Integer.parseInt(calculateResponse.amount);
                mView.chargeAmount(calculateResponse.charge, fee+"");
            }
        });
    }

//    @Override
//    public void chargeTransaction(String transactionId) {
//        cInterface.showProgressLoading();
//        charge(transactionId).subscribe(new Subscriber<QRTransactionResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                cInterface.hideProgresLoading();
//                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
//            }
//
//            @Override
//            public void onNext(QRTransactionResponse jsonObject) {
//                cInterface.hideProgresLoading();
//                mView.charge(jsonObject);
//            }
//        });
//    }

    Observable<CalculateResponse> calculateAmount(String amount, String type, String merchantId) {
        return cInterface.getService().calculateAmount(amount, type, merchantId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    Observable<QRTransactionResponse> charge(String transactionId) {
        return cInterface.getService().transactionConfirm(transactionId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
