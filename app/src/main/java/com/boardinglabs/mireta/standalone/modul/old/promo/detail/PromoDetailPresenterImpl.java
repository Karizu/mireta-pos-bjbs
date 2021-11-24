package com.boardinglabs.mireta.standalone.modul.old.promo.detail;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GPromo;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.promo.PromoInteractor;
import com.boardinglabs.mireta.standalone.modul.old.promo.PromoInteractorImpl;

import rx.Subscriber;

/**
 * Created by Dhimas on 3/4/18.
 */

public class PromoDetailPresenterImpl implements PromoDetailPresenter {
    private CommonInterface cInterface;
    private PromoInteractor mInteractor;
    private SuccessPromo sPromo;

    public PromoDetailPresenterImpl(CommonInterface cInterface, SuccessPromo listener) {
        this.cInterface = cInterface;
        mInteractor = new PromoInteractorImpl(this.cInterface.getService());
        sPromo = listener;
    }

    @Override
    public void getPromoDetail(String promoId) {
        cInterface.showProgressLoading();
        mInteractor.getPromoDetail(promoId).subscribe(new Subscriber<GPromo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(GPromo gPromo) {
                cInterface.hideProgresLoading();
                if (sPromo != null) {
                    sPromo.setData(gPromo);
                }

            }
        });
    }

    public interface SuccessPromo {
        void setData(GPromo promo);
    }
}
