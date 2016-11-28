package com.phuongptt.funnycal;

import com.phuongptt.funnycal.services.models.User;

/**
 * Created by tranvietthang on 11/28/16.
 */

public class Authentication {

    private static Authentication instance;

    public static  Authentication getInstance() {
        if (instance == null) {
            instance = new Authentication();
        }

        return instance;
    }


    private String token;
    private User profile;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }
}
