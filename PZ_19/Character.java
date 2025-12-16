package com.example.app_javafx.model;

public class Character {
    private String name;
    private String status;
    private String species;
    private String imageUrl;

    public Character() {
        // Пустой конструктор для Jackson/Gson
    }

    public Character(String name, String status, String species, String imageUrl) {
        this.name = name;
        this.status = status;
        this.species = species;
        this.imageUrl = imageUrl;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return name + " - " + status + " (" + species + ")";
    }
}