package com.example.golapp.models;

public class Cycle {
    private Integer id;
    private String name;
    private Integer school_id;
    private Gol gol;

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

    public Gol getGol() {
        return gol;
    }
}
