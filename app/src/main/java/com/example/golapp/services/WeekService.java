package com.example.golapp.services;

import com.example.golapp.models.School;
import com.example.golapp.models.Tutor;
import com.example.golapp.models.Week;
import com.example.golapp.requests.tutor.TutorStoreRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WeekService {
    @GET("/api/weeks")
    Call<BaseResponse<CollectionResponse<Week>>> index();

    @POST("/api/weeks")
    Call<BaseResponse<String>> store();

    @DELETE("/api/weeks/{id}")
    Call<BaseResponse<String>> delete(@Path("id") int id);
}
