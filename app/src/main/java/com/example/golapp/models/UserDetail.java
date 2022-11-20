package com.example.golapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDetail implements Serializable {
    private Person person;
    private ArrayList<String> roles;
    private String avatar;

    public Person getPerson() {
        return person;
    }

    public String getAvatar() {
        return avatar;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }
}
