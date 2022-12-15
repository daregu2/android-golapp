package com.example.golapp.services;

import com.example.golapp.models.UserDetail;
import com.example.golapp.requests.ResetPasswordRequest;
import com.example.golapp.responses.BaseResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileService {

    @POST("/api/user/upload-avatar")
    Call<BaseResponse<String>> uploadAvatar(@Body RequestBody body);

    @POST("/api/user/reset-password")
    Call<BaseResponse<String>> resetPassword(@Body ResetPasswordRequest request);

}
