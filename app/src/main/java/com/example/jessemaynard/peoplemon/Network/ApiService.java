package com.example.jessemaynard.peoplemon.Network;


import com.example.jessemaynard.peoplemon.Models.Account;
import com.example.jessemaynard.peoplemon.Models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jessemaynard on 11/7/16.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("token")
    Call<Account> login(@Field("username") String email,
                        @Field("password") String password,
                        @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/api/Account/Register")
    Call<Void> register(@Field("email") String email,
                        @Field("fullname") String fullame,
                        @Field("avatarbase64") String avatarBase64,
                        @Field("apikey") String apikey,
                        @Field("password") String password);

    @POST("/v1/User/CheckIn")
    Call<Void> checkIn(@Body User user);

    @GET("/v1/User/Nearby")
    Call<User[]> nearby(@Query("radiusInMeters") Integer radiusInMeters);


    @FormUrlEncoded
    @POST("/v1/User/Catch")
    Call<Void> catchUser(@Field("caughtUserId") String userId, @Field("radiusInMeters") Integer radiusInMeters);


    @GET("/v1/User/Caught")
    Call<User[]> caught();

    @GET("/api/Account/UserInfo")
    Call<Account> getInfo();

    @POST("/api/Account/UserInfo")
    Call<Void> postInfo(@Body Account account);
}
