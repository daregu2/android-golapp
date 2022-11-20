package com.example.golapp.services;

import com.example.golapp.requests.gol.GolRequest;
import com.example.golapp.requests.tutor.TutorStoreRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.responses.GolResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GolService {

    @GET("/api/gols")
    Call<BaseResponse<CollectionResponse<GolResponse>>> index();

    @POST("/api/gols")
    Call<BaseResponse<String>> store(@Body RequestBody body);
//
//    @PUT("/api/gols/{id}")
//    Call<BaseResponse<String>> update(@Path("id") int id, @Body TutorStoreRequest request);
//
//    @DELETE("/api/gols/{id}")
//    Call<BaseResponse<String>> delete(@Path("id") int id);

}
