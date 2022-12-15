package com.example.golapp.services;

import com.example.golapp.models.Student;
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

public interface StudentService {

    @GET("/api/students")
    Call<BaseResponse<CollectionResponse<Student>>> index();

    @GET("/api/students/{id}")
    Call<BaseResponse<Student>> show(@Path("id") int id);

    @POST("/api/students")
    Call<BaseResponse<String>> store(@Body TutorStoreRequest request);

    @PUT("/api/students/{id}")
    Call<BaseResponse<String>> update(@Path("id") int id, @Body TutorStoreRequest request);

    @DELETE("/api/students/{id}")
    Call<BaseResponse<String>> delete(@Path("id") int id);

    @POST("/api/students/{id}/set-lider")
    Call<BaseResponse<String>> setLider(@Path("id") int id);

}
