package com.boardinglabs.mireta.standalone.modul.home;

import com.boardinglabs.mireta.standalone.component.network.entities.Transaction;
import com.boardinglabs.mireta.standalone.component.network.entities.TransactionResponse;

import java.util.List;

public interface HomeView {
    void onSuccessGetLatestTransactions(List<Transaction> transactions);
    void onSuccessGetLatestTransactionsNow(List<TransactionResponse> transactions);
}
