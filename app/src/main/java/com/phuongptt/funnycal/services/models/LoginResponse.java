package com.phuongptt.funnycal.services.models;

import com.google.gson.annotations.SerializedName;



/**
 * Created by tranvietthang on 11/27/16.
 */


public class LoginResponse extends BaseResponse {
    @SerializedName("token")
    public String token;

    @SerializedName("data")
    public User profile;
}
