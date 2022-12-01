package com.example.golapp.models;

import java.io.Serializable;

public class Event implements Serializable {
    private Integer id;
    private String name;
    private String banner;
    private String programmed_at;
    private String status;
    private String start_at;
    private String end_at;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getProgrammed_at() {
        return programmed_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getStatus() {
        return status;
    }
}
