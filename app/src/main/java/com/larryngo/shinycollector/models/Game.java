package com.larryngo.shinycollector.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private int id;
    private String name;
    private final String token;
    private byte[] image;

    public Game() {
        this.id = 0;
        this.name = "Red";
        this.image = null;
        this.token = "Red";
    }

    public Game(int id, String text, byte[] image, String token)
    {
        this.id = id;
        this.name = text;
        this.image = image;
        this.token = token;
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


    //Determines the generation of the game. Can be used to determine what pokemon
    //can be acquired. (Red/Blue/Yellow can go up to 151, G/S/C up to 251, etc.)
    public int getGeneration() {
        switch(token) {
            case "Red":
            case "Blue":
            case "Green":
            case "Yellow":
                return 1;
            case "Gold":
            case "Silver":
            case "Crystal":
                return 2;
            case "Ruby":
            case "Sapphire":
            case "Emerald":
            case "FireRed":
            case "LeafGreen":
            case "Colosseum":
            case "XD-Gale-of-Darkness":
                return 3;
            case "Diamond":
            case "Pearl":
            case "Platinum":
            case "HeartGold":
            case "SoulSilver":
            case "Rumble":
            case "Battle-Revolution":
                return 4;
            case "Black":
            case "White":
            case "Black-2":
            case "White-2":
                return 5;
            case "X":
            case "Y":
            case "Omega-Ruby":
            case "Alpha-Sapphire":
            case "Go":
                return 6;
            case "Sun":
            case "Moon":
            case "Ultra-Sun":
            case "Ultra-Moon":
            case "Pikachu":
            case "Eevee":
                return 7;
            case "Sword":
            case "Shield":
            case "Mystery-Dungeon-Rescue-DX":
                return 8;
        }
        return -1;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.createByteArray();
        token = in.readString();
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
        dest.writeString(token);
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