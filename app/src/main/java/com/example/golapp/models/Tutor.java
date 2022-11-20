package com.example.golapp.models;

import java.io.Serializable;

public class Tutor extends Person implements Serializable {
    private String cycle;
    private String school;

    public Tutor(){
        super();
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
