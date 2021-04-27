package com.example.searchshop.Response;

import com.example.searchshop.Models.ShopModel;

import java.util.ArrayList;
import java.util.List;

public class ShopResponse {


    private List<ShopModel> shopList = null;

    public ShopResponse() {
        shopList = new ArrayList<>();
    }

    public List<ShopModel> getShopList() {
        return shopList;
    }


}
