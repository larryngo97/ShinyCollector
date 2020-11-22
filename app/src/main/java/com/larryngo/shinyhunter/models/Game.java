package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private int id;
    private String name;
    private byte[] image;
    private int generation;

    public Game() {
        this.id = 0;
        this.name = "Pokemon Red";
        this.image = null;
        this.generation = 1;
    }

    public Game(int id, String text, byte[] image, int generation)
    {
        this.id = id;
        this.name = text;
        this.image = image;
        this.generation = generation;
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

    public void setName(String game_title) {
        this.name = game_title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] game_image) {
        this.image = game_image;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.createByteArray();
        generation = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByteArray(image);
        dest.writeInt(generation);
    }

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