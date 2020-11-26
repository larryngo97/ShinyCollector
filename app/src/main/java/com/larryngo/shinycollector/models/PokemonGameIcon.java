package com.larryngo.shinycollector.models;

public class PokemonGameIcon {
    private String name;
    private String image_url;

    public PokemonGameIcon(String title, String iconUrl) {
        this.name = title;
        this.image_url = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
