package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
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

    protected Game(Parcel in) {
        game_title = in.readString();
        game_image = in.createByteArray();
        generation = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(game_title);
        dest.writeByteArray(game_image);
        dest.writeInt(generation);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}