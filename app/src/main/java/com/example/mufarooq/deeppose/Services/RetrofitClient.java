package com.example.mufarooq.deeppose.Services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://172.16.11.188:9090";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (mInstance == null)
            mInstance = new RetrofitClient();

        return mInstance;
    }

    public DeepPoseApi getApi(){
        return retrofit.create(DeepPoseApi.class);
    }
}