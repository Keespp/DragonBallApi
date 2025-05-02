package com.keesp.dragonballapi;

public class Character {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private String race;
    private String ki;
    private String maxKi;
    private String gender;

    public Character(int id, String name, String imageUrl, String description, String race, String ki, String maxKi, String gender) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.race = race;
        this.ki = ki;
        this.maxKi = maxKi;
        this.gender = gender;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getRace() { return race; }
    public String getKi() { return ki; }
    public String getMaxKi() { return maxKi; }
    public String getGender() { return gender; }
}
