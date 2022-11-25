package com.example.golapp.services;

import com.example.golapp.models.Topic;
import com.example.golapp.requests.topic.TopicRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TopicService {
    @GET("/api/topics")
    Call<BaseResponse<CollectionResponse<Topic>>> index();

    @PUT("/api/topics/{id}")
    Call<BaseResponse<String>> update(@Path("id") int id,@Body TopicRequest body);

    @DELETE("/api/topics/{id}")
    Call<BaseResponse<String>> delete(@Path("id") int id);
}
