package com.boardinglabs.mireta.standalone.component.network.response;

import com.boardinglabs.mireta.standalone.component.network.entities.Business;
import com.boardinglabs.mireta.standalone.component.network.entities.StockLocation;
import com.boardinglabs.mireta.standalone.component.network.entities.User;

public class LoginResponse extends BaseResponse{
    public String access_token;
    public User data;
    public Business business;
    public StockLocation user_location;
}
