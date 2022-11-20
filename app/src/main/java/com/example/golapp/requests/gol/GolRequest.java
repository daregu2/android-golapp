package com.example.golapp.requests.gol;

import com.example.golapp.models.Gol;

public class GolRequest extends Gol {
    private String photo;

    public GolRequest(){
        super();
    }

    public String getPhoto() {
        return photo;
    }
}
