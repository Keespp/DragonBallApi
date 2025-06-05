package com.keesp.dragonballapi;

import java.util.List;

public class Planet {
    private int id;
    private String name;
    private String description;
    private String image;
    private boolean isDestroyed;
    private List<Character> characters;

    public Planet(int id, String name, String description, String image, boolean isDestroyed,List<Character> characters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.isDestroyed = isDestroyed;
        this.characters = characters;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public boolean isDestroyed() { return isDestroyed; }
    public List<Character> getCharacters() { return characters; }
}

