package com.example.golapp.requests.eventperson;

public class EventPersonUpdateRequest {
    private Integer person_id;
    private Boolean present;

    public EventPersonUpdateRequest(Integer person_id, Boolean present) {
        this.person_id = person_id;
        this.present = present;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }
}
