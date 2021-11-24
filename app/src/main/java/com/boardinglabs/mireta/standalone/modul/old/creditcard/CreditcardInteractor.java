package com.boardinglabs.mireta.standalone.modul.old.creditcard;

import com.google.gson.JsonObject;
import com.boardinglabs.mireta.standalone.component.network.gson.GCreditCard;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 2/2/18.
 */

public interface CreditcardInteractor {
    Observable<JsonObject> newTransaction(String merchant_id, String cardType, String amount_transaction, String bank_id, String notes, String card_token, List<String> imageId);

    Observable<JsonObject> newTransactionAddRecipient(String merchantName, String accountName, String accountNo, String cardType,
                                                      String amount_transaction, String bank_id, List<String> imageId,
                                                      String notes, String card_token, String phoneNumber, String isFavorite);

    Observable<GCreditCard> newTransactionMidtrans(String merchant_id, String cardType, String amount_transaction, String bank_id, String notes, List<String> imageId, String voucherId);

    Observable<GCreditCard> fastTransactionMidtrans(String merchant_id, String cardType,
                                                    String amount_transaction, String bank_id,
                                                    String notes, List<String> imageId,
                                                    String voucherId, String custName,
                                                    String custEmail, String custPhone,
                                                    String qrId, String hashCard);

    Observable<JsonObject> attachCreditCard(String tokenId);

    Observable<JsonObject> addCreditCard(String tokenId, String cardhash, String cardName, String expiryMonth, String expiryYear, String expiredToken);
}
