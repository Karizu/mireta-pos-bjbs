package com.boardinglabs.mireta.standalone.modul.old.scanqr;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.QRTransactionResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/1/18.
 */

public class ScanQRPresenterImpl implements ScanQRPresenter {
    private NetworkService mService;
    private CommonInterface cInterface;
    private ScanQRView qrView;

    public ScanQRPresenterImpl(NetworkService service, CommonInterface cInterface, ScanQRView qrView) {
        mService = service;
        this.cInterface = cInterface;
        this.qrView = qrView;
    }

    @Override
    public void scanCode(String code) {
        cInterface.showProgressLoading();
        scanQRCode(code).subscribe(new Subscriber<QRTransactionResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(QRTransactionResponse qrTransactionResponse) {
                cInterface.hideProgresLoading();
                if (qrTransactionResponse.item.is_used.equalsIgnoreCase("0")) {
                    qrView.openTransactionActivity(qrTransactionResponse.item.merchant_id, qrTransactionResponse.item.amount,
                            qrTransactionResponse.item.notes, qrTransactionResponse.item.id, qrTransactionResponse.item.merchant.name,
                            qrTransactionResponse.item.voucher_id, qrTransactionResponse.item.type);
                } else {
                    cInterface.onFailureRequest("ID ORDER TERPAKAI, Hubungi merchant anda untuk membuat order baru");
                }
            }
        });
    }

    Observable<QRTransactionResponse> scanQRCode(String code) {
        return mService.getQrData(code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
