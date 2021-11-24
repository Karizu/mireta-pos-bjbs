package com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard;

import com.boardinglabs.mireta.standalone.component.network.gson.GCard;

import java.util.List;

/**
 * Created by Dhimas on 2/14/18.
 */

public interface SavedCreditcardView {
    void onSuccessListCreditcard(List<GCard> cardList);

    void onSuccessDeleteCard();

}
