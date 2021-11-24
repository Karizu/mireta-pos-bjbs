package com.boardinglabs.mireta.standalone.component.network.response;

import com.boardinglabs.mireta.standalone.component.network.entities.Brand;
import com.boardinglabs.mireta.standalone.component.network.entities.Business;
import com.boardinglabs.mireta.standalone.component.network.entities.StockLocation;
import com.boardinglabs.mireta.standalone.component.network.entities.User;

public class SignUpBusinessResponse extends BaseResponse{
    public String token;
    public User data;
    public Business business;
    public Brand brand;
    public StockLocation stock_location;
}
