package com.example.golapp.requests.topic;

public class TopicRequest {
    private String name;
    private String description;
    private String resource_link;

    public String getResource_link() {
        return resource_link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResource_link(String resource_link) {
        this.resource_link = resource_link;
    }
}
