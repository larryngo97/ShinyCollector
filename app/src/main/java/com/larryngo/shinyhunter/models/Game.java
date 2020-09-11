package com.larryngo.shinyhunter.models;

import android.graphics.Bitmap;

import com.larryngo.shinyhunter.R;

import java.util.Arrays;
import java.util.List;

public class Game {
    private String game_title;
    private byte[] game_image;
    private int generation;

    public Game(String text, byte[] image, int generation)
    {
        this.game_title = text;
        this.game_image = image;
        this.generation = generation;
    }

    public String getName() {
        return game_title;
    }

    public void setName(String game_title) {
        this.game_title = game_title;
    }

    public byte[] getImage() {
        return game_image;
    }

    public void setImage(byte[] game_image) {
        this.game_image = game_image;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }
}
