package com.example.golapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    private Integer id;
    private String name;
    private String banner;
    private String programmed_at;
    private String status;
    private String start_at;
    private String end_at;
    private Topic topic;
    private ArrayList<Person> people;

    public Topic getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getProgrammed_at() {
        return programmed_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getStatus() {
        String estado = "";
        switch (this.status){
            case "P":
                estado = "Programado";
                break;
            case "F":
                estado = "Finalizado";

        }
        return estado;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }
}
