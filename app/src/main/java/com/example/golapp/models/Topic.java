package com.example.golapp.models;

import java.io.Serializable;

public class Topic implements Serializable {
    private Integer id;
    private String name;
    private String resource_link;
    private Integer grade;
    private String description;
    private boolean is_active;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getDescription() {
        return description;
    }

    public String getResource_link() {
        return resource_link;
    }

    public boolean isIs_active() {
        return is_active;
    }
}
