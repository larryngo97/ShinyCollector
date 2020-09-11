package com.larryngo.shinyhunter.models;

import android.graphics.Bitmap;

public class Platform {
    private String name;
    private byte[] image;

    public Platform (String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
