package com.keesp.dragonballapi;

public class Transformation {
    private String name;
    private String ki;
    private String image;

    public Transformation(String name,String ki, String image) {
        this.name = name;
        this.ki = ki;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getKi() {
        return ki;
    }

    public String getImage() {
        return image;
    }
}
