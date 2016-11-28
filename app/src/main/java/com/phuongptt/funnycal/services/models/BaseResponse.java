package com.phuongptt.funnycal.services.models;

import com.google.gson.annotations.SerializedName;


/**
 * Format for all response body
 * Created by tranvietthang on 7/3/16.
 */


public class BaseResponse {

    /**
     * Tasks are done successfully or not
     */
    @SerializedName("success")
    public boolean success;

    /**
     * Message to notify user if error occurs
     */
    @SerializedName("message")
    public String message;

}
