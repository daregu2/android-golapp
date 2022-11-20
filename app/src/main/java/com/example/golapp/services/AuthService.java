package com.example.golapp.services;

import com.example.golapp.models.UserDetail;
import com.example.golapp.requests.LoginRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/auth/login")
    Call<BaseResponse<LoginResponse>> login(@Body LoginRequest request);

    @GET("/api/user/profile")
    Call<BaseResponse<UserDetail>> fetchProfile();

    @POST("/api/auth/logout")
    Call<BaseResponse<String>> logout();


}
