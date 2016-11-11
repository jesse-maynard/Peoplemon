package com.example.jessemaynard.peoplemon.Network;

import com.example.jessemaynard.peoplemon.PeopleMonApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jessemaynard on 11/7/16.
 */

public class RestClient {
    private ApiService apiService;

    public RestClient(){
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        Gson gson = builder.create();

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new SessionRequestInterceptor()).addInterceptor(log).build();

        Retrofit restAdapter = new Retrofit.Builder().baseUrl(PeopleMonApplication.API_BASE_URL)
                .client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}