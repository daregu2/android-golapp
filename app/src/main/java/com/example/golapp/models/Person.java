package com.example.golapp.models;

import java.io.Serializable;

public class Person implements Serializable {
    private Integer id;
    private String names;
    private String last_names;
    private Integer code;
    private String email;
    private Integer phone;
    private Cycle cycle;
    private Pivot pivot;

    public Integer getId() {
        return id;
    }

    public String getNames() {
        return names;
    }

    public String getLastNames() {
        return last_names;
    }

    public Integer getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPhone() {
        return phone;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public Pivot getPivot() {
        return pivot;
    }
}
