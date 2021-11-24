package com.boardinglabs.mireta.standalone.modul.transactions.items.pembayaran;

public class Items {

    private String id;
    private String brand_id;
    private String business_id;
    private String category_id;
    private String description;
    private String name;
    private long order_qty;
    private String price;
    private String partner_item_id;
    private String partner_item_expired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(long order_qty) {
        this.order_qty = order_qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPartner_item_id() {
        return partner_item_id;
    }

    public void setPartner_item_id(String partner_item_id) {
        this.partner_item_id = partner_item_id;
    }

    public String getPartner_item_expired() {
        return partner_item_expired;
    }

    public void setPartner_item_expired(String partner_item_expired) {
        this.partner_item_expired = partner_item_expired;
    }
}
