package com.example.golapp.responses;

import com.example.golapp.models.Gol;

public class GolResponse extends Gol {
    private String photo;

    public GolResponse(){
        super();
    }

    public String getPhoto() {
        return photo;
    }
}
