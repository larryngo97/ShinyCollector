package com.larryngo.shinyhunter.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Pokemon {
    private int id;
    private String name;
    private ArrayList<String> types;
    private String image_url;
    private Bitmap bitmap;
    private int weight;

    public Pokemon() {
        id = 1;
        name = "bulbasaur";
        types = new ArrayList<>();
        types.add("grass");
        types.add("poison");
        image_url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png";
        bitmap = null;
    }

    public Pokemon(int id, String name, ArrayList<String> types, String image_url) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.image_url = image_url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
