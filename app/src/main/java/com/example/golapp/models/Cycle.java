package com.example.golapp.models;

import java.io.Serializable;

public class Cycle implements Serializable {
    private Integer id;
    private String name;
    private Integer school_id;
    private Boolean is_active;
    private Gol gol;
    private School school;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public Cycle() {

    }

    public Boolean getIs_active() {
        return is_active;
    }

    public School getSchool() {
        return school;
    }

    public Gol getGol() {
        return gol;
    }
}
