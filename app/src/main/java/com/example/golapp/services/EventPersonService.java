package com.example.golapp.services;

import com.example.golapp.models.Event;
import com.example.golapp.requests.eventperson.EventPersonUpdateRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventPersonService {

    @GET("/api/event-person")
    Call<Event> index();

//    @POST("/api/event-person")
//    Call<BaseResponse<String>> store();

    @PUT("/api/event-person/{id}")
    Call<BaseResponse<String>> update(@Path("id") int id, @Body EventPersonUpdateRequest request);
//
//    @DELETE("/api/event-person/{id}")
//    Call<BaseResponse<String>> delete(@Path("id") int id);

}
