package com.example.golapp.responses;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    private Boolean success;
    private String message;

    @SerializedName("result")
    private T result;

    public T getResult() {
        return this.result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
