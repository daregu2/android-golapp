package com.example.golapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class School implements Serializable {
    private Integer id;
    private String name;
    private ArrayList<Cycle> cycles;
    private boolean isExpandable;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Cycle> getCycles() {
        return cycles;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
