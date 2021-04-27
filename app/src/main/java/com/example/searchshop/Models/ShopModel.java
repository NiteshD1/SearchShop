package com.example.searchshop.Models;

import java.io.Serializable;
import java.util.List;

public class ShopModel implements Serializable {

    private String shopName;
    private int discount;
    private int cashback;
    private String address;
    private List<String> productsList;

    public String getShopName() {
        return shopName;
    }

    public int getDiscount() {
        return discount;
    }

    public int getCashback() {
        return cashback;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getProductsList() {
        return productsList;
    }

    public ShopModel(String shopName, int discount, int cashback, String address, List<String> productsList) {
        this.shopName = shopName;
        this.discount = discount;
        this.cashback = cashback;
        this.address = address;
        this.productsList = productsList;
    }
}
