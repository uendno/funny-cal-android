package com.phuongptt.funnycal.services;

import com.phuongptt.funnycal.services.models.BaseResponse;
import com.phuongptt.funnycal.services.models.LoginResponse;
import com.phuongptt.funnycal.services.models.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Contains all the services we need to connect to server
 * Created by tranvietthang on 7/3/16.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<LoginResponse> register(@Field("username") String username, @Field("password") String password, @Field("full_name") String fullName);

    @FormUrlEncoded
    @PUT("users/updateScore")
    Call<BaseResponse> updateScore(@Field("score") int score,  @Field("token") String token);

    @GET("users")
    Call<UsersResponse> getAllUsers(@Header("token") String token);
}
