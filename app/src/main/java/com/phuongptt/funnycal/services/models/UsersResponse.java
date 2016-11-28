package com.phuongptt.funnycal.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tranvietthang on 11/27/16.
 */


public class UsersResponse extends BaseResponse {

    @SerializedName("data")
    public List<User> users;
}
