package com.larryngo.shinycollector.models;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PokemonList {
    private int id;
    private String name;
    private String url;
    private ArrayList<PokemonList> results;
    private Pokemon Pokemon;

    public PokemonList() {
        id = 0;
        name = "Missingno";
    }

    public Pokemon getPokemon() {
        return Pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        Pokemon = pokemon;
    }

    public ArrayList<PokemonList> getResults() {
        return results;
    }

    public int getId() {
        //return id;
        String[] newUrl = url.split("/");
        return Integer.parseInt(newUrl[newUrl.length - 1]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
