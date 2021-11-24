package com.boardinglabs.mireta.standalone.modul.old.creditcard;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Dhimas on 2/2/18.
 */

@Parcel
public class Transaction {
    String id;
    String merchantId;
    String amountTransaction;
    String totalAmount;
    String charge;
    String notes;
    String bankId;
    String merchantName;
    String merchantAccountName;
    String merchantAccountNo;
    String merchantContactNo;
    String cardType;
    String cardToken;
    String phoneNumber;
    boolean newRecipient;
    boolean isFavorite;
    List<String> imageList;
    List<String> IdImage;
    String amountVoucher;

    public String getAmountVoucher() {
        return amountVoucher;
    }

    public void setAmountVoucher(String amountVoucher) {
        this.amountVoucher = amountVoucher;
    }

    public List<String> getIdImage() {
        return IdImage;
    }

    public void setIdImage(List<String> idImage) {
        IdImage = idImage;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(String amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAccountName() {
        return merchantAccountName;
    }

    public void setMerchantAccountName(String merchantAccountName) {
        this.merchantAccountName = merchantAccountName;
    }

    public String getMerchantAccountNo() {
        return merchantAccountNo;
    }

    public void setMerchantAccountNo(String merchantAccountNo) {
        this.merchantAccountNo = merchantAccountNo;
    }

    public String getMerchantContactNo() {
        return merchantContactNo;
    }

    public void setMerchantContactNo(String merchantContactNo) {
        this.merchantContactNo = merchantContactNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public boolean isNewRecipient() {
        return newRecipient;
    }

    public void setNewRecipient(boolean newRecipient) {
        this.newRecipient = newRecipient;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
