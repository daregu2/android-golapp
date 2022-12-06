package com.example.golapp.models;

public class Pivot {
    private Integer event_id;
    private Integer person_id;
    private Boolean present;

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public Integer getPerson_id() {
        return person_id;
    }
}
