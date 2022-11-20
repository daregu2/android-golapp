package com.example.golapp.api;

import com.example.golapp.AppConstants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;

    private static Gson getGsonBuilder(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private static OkHttpClient getOkHttpClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.addInterceptor(new AuthenticationInterceptor()).build();
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(RetrofitInstance.getGsonBuilder()))
                    .baseUrl(AppConstants.BASE_URL)
                    .client(RetrofitInstance.getOkHttpClient())
                    .build();
        }
        return retrofit;
    }
}
