package com.boardinglabs.mireta.standalone.modul.old.jiwasraya;

import android.text.TextUtils;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
//import com.boardinglabs.mireta.smansa.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import org.json.JSONException;

import rx.Subscriber;

/**
 * Created by Dhimas on 12/23/17.
 */

public class JiwasrayaPresenterImpl implements JiwasrayaPresenter {
    JiwasrayaView mView;
    CommonInterface cInterface;
    private JiwasrayaInteractor mInteractor;

    public JiwasrayaPresenterImpl(CommonInterface commonInterface, JiwasrayaView view) {
        cInterface = commonInterface;
        mView = view;
        mInteractor = new JiwasrayaInteractorImpl(this.cInterface.getService());
    }

    private Boolean isValidData(String name, String mobile, String email) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(email)) {
            cInterface.onFailureRequest("Harap isi kolom yang masih kosong");
            return false;
        }
        return true;
    }

    @Override
    public void setInquiry(String serviceId, String customerNo, boolean isUsing, String amount) {
        cInterface.showProgressLoading();
        if (isUsing) {
            mInteractor.setInquiry(serviceId, customerNo, amount).subscribe(new Subscriber<TransactionResponse>() {
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
                    if (transactionResponse.success) {
                        try {
                            mView.onSuccessInquiry(transactionResponse.transactions);
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                        }
                    } else {
                        cInterface.onFailureRequest(transactionResponse.message);
                    }

                }
            });
        } else {
            mInteractor.getTransaction(serviceId, customerNo).subscribe(new Subscriber<TransactionResponse>() {
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
                        mView.onSuccessInquiry(transactionResponse.transactions);
                    } catch (JSONException e) {
//                        e.printStackTrace();
                        cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                    }
                }
            });
        }
    }

    @Override
    public void updateAmountInquiry(String transactionId, String amount) {
        cInterface.showProgressLoading();
        mInteractor.updateAmountInquiry(transactionId, amount).subscribe(new Subscriber<TransactionResponse>() {
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
                try {
                    mView.onSuccessUpdateAmountInquiry(transactionResponse.transactions);
                } catch (JSONException e) {
//                    e.printStackTrace();
                    cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                }
            }
        });
    }
}
