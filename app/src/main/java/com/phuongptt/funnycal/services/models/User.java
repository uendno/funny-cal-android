package com.phuongptt.funnycal.services.models;

import com.google.gson.annotations.SerializedName;


/**
 * Created by tranvietthang on 11/27/16.
 */


public class User {
    @SerializedName("_id")
    public String id;

    @SerializedName("username")
    public String username;

    @SerializedName("score")
    public int score;

    @SerializedName("fullName")
    public String fullName;
}
