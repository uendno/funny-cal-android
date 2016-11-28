package com.phuongptt.funnycal.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Contain a retrofit client for connecting to server
 * Created by tranvietthang on 7/3/16.
 */
public class RetrofitClient {

    private static final String BASE_URL = "http://128.199.187.31:3333";
    private static ApiService apiService;

    private RetrofitClient() {

    }

    /**
     * Get an ApiService. Retrofit will create a object base on our interface to do works
     * @return an ApiService
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }
}
