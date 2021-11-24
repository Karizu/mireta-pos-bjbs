package com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.gson.GCard;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/6/18.
 */

public class SavedCreditcardPresenterImpl implements SavedCreditcardPresenter {
    private SavedCreditcardView mView;
    private CommonInterface cInterface;

    public SavedCreditcardPresenterImpl(SavedCreditcardView view, CommonInterface cInterface) {
        this.cInterface = cInterface;
        mView = view;
    }

    @Override
    public void getListCard() {
        cInterface.showProgressLoading();
        getCreditCardMidtransList(cInterface.getService()).subscribe(new Subscriber<List<GCard>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
//                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(List<GCard> cardList) {
                cInterface.hideProgresLoading();
                mView.onSuccessListCreditcard(cardList);
            }
        });
    }

    @Override
    public void showList(int countCard) {

    }

    @Override
    public void onSelectCard(String tokenId, String cardName, String lastDigit) {

    }

    @Override
    public void onDeleteCard(String cardId) {
        cInterface.showProgressLoading();
        deleteCreditCardMidtrans(cInterface.getService(), cardId).subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                cInterface.hideProgresLoading();
                mView.onSuccessDeleteCard();
            }
        });
    }

    @Override
    public void getSelectCreditCard() {

    }

    private Observable<List<GCard>> getCreditCardList(NetworkService networkService) {
        return networkService.getCreditCardList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    private Observable<List<GCard>> getCreditCardMidtransList(NetworkService networkService) {
        return networkService.getCreditCardMidtransList("midtrans").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    private Observable<JsonObject> deleteCreditCard(NetworkService networkService, String cardId) {
        return networkService.deleteCard(cardId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    private Observable<JsonObject> deleteCreditCardMidtrans(NetworkService networkService, String cardId) {
        return networkService.deleteCardMidtrans(cardId, "midtrans").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
