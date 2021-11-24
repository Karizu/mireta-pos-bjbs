package com.boardinglabs.mireta.standalone.modul.old.oldhome.transaction;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.BankTransferResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.CashbackResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TopupResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.TransactionHistoryResponse;

import rx.Observable;

/**
 * Created by Dhimas on 12/21/17.
 */

public interface TransactionInteractor {
    Observable<TransactionHistoryResponse> getTransaction(int page);

    Observable<TransactionHistoryResponse> getTransactionPpob(int page);

    Observable<TopupResponse> getTopup(int page);

    Observable<CashbackResponse> getCashback(int page);

    Observable<BankTransferResponse> updateBank(String orderId, String bankId, String accountId);
}
