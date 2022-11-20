package com.example.golapp.services;

import com.example.golapp.models.Tutor;
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

public interface TutorService {

    @GET("/api/tutors")
    Call<BaseResponse<CollectionResponse<Tutor>>> index();

    @POST("/api/tutors")
    Call<BaseResponse<String>> store(@Body TutorStoreRequest request);

    @PUT("/api/tutors/{id}")
    Call<BaseResponse<String>> update(@Path("id") int id, @Body TutorStoreRequest request);

    @DELETE("/api/tutors/{id}")
    Call<BaseResponse<String>> delete(@Path("id") int id);

}
