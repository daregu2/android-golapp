package com.example.golapp.services;

import com.example.golapp.models.School;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SchoolService {
    @GET("/api/schools")
    Call<BaseResponse<CollectionResponse<School>>> fetchSchools();
}
