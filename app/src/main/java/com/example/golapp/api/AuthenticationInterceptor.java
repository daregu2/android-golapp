package com.example.golapp.api;

import androidx.annotation.NonNull;

import com.example.golapp.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    TokenManager tokenManager;

    public AuthenticationInterceptor() {
        this.tokenManager = new TokenManager();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().header("Authorization", "Bearer " + this.tokenManager.getToken()).header("Accept", "application/json").build();
        return chain.proceed(request);
    }
}
