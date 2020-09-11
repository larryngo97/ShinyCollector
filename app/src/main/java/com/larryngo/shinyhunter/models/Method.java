package com.larryngo.shinyhunter.models;

import java.util.Arrays;
import java.util.List;

public class Method {
    private int id;
    private String name;
    private List<String> list_methods_names = Arrays.asList("DV Method (Gen 1)", "Random Encounters", "Soft Reset", "Run Away", "Egg Hatching",
            "Masuda Method", "Pokeradar", "Chain Fishing", "Friend Safari", "Horde Hunting", "Dexnav", "SOS", "Ultra Wormhole", "Catch Combo",
            "Island Scan", "Fossil Restoration", "Other");

    public Method(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMethodList() {
        return list_methods_names;
    }
}
