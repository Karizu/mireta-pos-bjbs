package com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard;

/**
 * Created by Dhimas on 2/6/18.
 */

public interface SavedCreditcardPresenter {
    void getListCard();

    void showList(int countCard);

    void onSelectCard(String tokenId, String cardName, String lastDigit);

    void onDeleteCard(String cardId);

    void getSelectCreditCard();
}
