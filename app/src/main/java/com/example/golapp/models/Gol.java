package com.example.golapp.models;

import java.io.Serializable;

public class Gol implements Serializable {
    private Integer id;
    private String name;
    private String motto;
    private String chant;
    private String verse;
    private String photo;

    public Gol() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMotto() {
        return motto;
    }

    public String getChant() {
        return chant;
    }

    public String getVerse() {
        return verse;
    }

    public String getPhoto() {
        return photo;
    }
}
