package com.example.golapp.models;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String name;
    private String avatar;
    private boolean is_active;
    private boolean is_lider;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public boolean isIs_lider() {
        return is_lider;
    }

    public String getAvatar() {
        return avatar;
    }
}
