package com.example.golapp.services;

import com.example.golapp.models.Event;
import com.example.golapp.models.Student;
import com.example.golapp.requests.event.EventRequest;
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

public interface EventService {

    @GET("/api/events")
    Call<BaseResponse<CollectionResponse<Event>>> index();

    @POST("/api/events")
    Call<BaseResponse<String>> store(@Body EventRequest request);

    @PUT("/api/events/{id}")
    Call<BaseResponse<String>> update(@Path("id") int id, @Body EventRequest request);

    @DELETE("/api/events/{id}")
    Call<BaseResponse<String>> delete(@Path("id") int id);

}
