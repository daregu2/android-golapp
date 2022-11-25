package com.example.golapp.models;

import java.util.ArrayList;

public class Week {
    private Integer id;
    private String event_date;
    private ArrayList<Topic> topics;
    private boolean isExpandable;


    public Week(){

    }

    public Integer getId() {
        return id;
    }

    public String getEvent_date() {
        return event_date;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
