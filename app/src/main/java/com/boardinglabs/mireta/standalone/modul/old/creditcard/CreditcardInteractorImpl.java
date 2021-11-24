package com.boardinglabs.mireta.standalone.modul.old.creditcard;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 2/2/18.
 */

public class CreditcardInteractorImpl implements CreditcardInteractor {
    private NetworkService mService;

    public CreditcardInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<JsonObject> newTransaction(String merchant_id, String cardType, String amount_transaction, String bank_id, String notes, String card_token, List<String> imageId) {
        return null;
    }

    @Override
    public Observable<JsonObject> newTransactionAddRecipient(String merchantName, String accountName, String accountNo, String cardType, String amount_transaction, String bank_id, List<String> imageId, String notes, String card_token, String phoneNumber, String isFavorite) {
        return null;
    }

    @Override
    public Observable<GCreditCard> newTransactionMidtrans(String merchant_id, String cardType, String amount_transaction, String bank_id, String notes, List<String> imageId, String voucherId) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .addEncoded("merchant_id", merchant_id)
                .addEncoded("card_type", cardType)
                .addEncoded("amount_transaction", amount_transaction)
                .addEncoded("bank_id", bank_id)
                .addEncoded("notes", notes)
                .addEncoded("voucher_id", voucherId);


        /* open comment to multiple upload image*/
        if (imageId != null) {
            for (String imgId : imageId) {
                formBuilder.addEncoded("image_id[" + imageId.indexOf(imgId) + "]", imgId);
            }
        }

        RequestBody formBody = formBuilder.build();
        return mService.postTransactionRecipientMidtrans(formBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<GCreditCard> fastTransactionMidtrans(String merchant_id, String cardType,
                                                          String amount_transaction, String bank_id,
                                                          String notes, List<String> imageId,
                                                          String voucherId, String custName,
                                                          String custEmail, String custPhone,
                                                          String qrId, String hashCard) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .addEncoded("merchant_id", merchant_id)
                .addEncoded("card_type", cardType)
                .addEncoded("amount_transaction", amount_transaction)
                .addEncoded("notes", notes)
                .addEncoded("voucher_id", voucherId)
                .addEncoded("customer_name", custName)
                .addEncoded("customer_email", custEmail)
                .addEncoded("voucher_object_type", "merchants")
                .addEncoded("qr_id", qrId)
                .addEncoded("card_hash",hashCard)
                .addEncoded("customer_phone", custPhone);

        RequestBody formBody = formBuilder.build();
        return mService.postTransactionRecipientMidtrans(formBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<JsonObject> attachCreditCard(String tokenId) {
        return mService.attachCreditCard(tokenId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<JsonObject> addCreditCard(String tokenId, String cardhash, String cardName, String expiryMonth, String expiryYear, String expiredToken) {
        return mService.addCreditCard("midtrans", tokenId, cardhash, cardName, expiryMonth, expiryYear, expiredToken).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
