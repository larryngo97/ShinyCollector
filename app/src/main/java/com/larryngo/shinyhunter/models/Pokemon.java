package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Pokemon implements Parcelable {
    private int id;
    private String name;
    private ArrayList<String> types;
    private String image_url;
    private byte[] image;
    private int weight;

    public Pokemon() {
        id = 1;
        name = "bulbasaur";
        types = new ArrayList<>();
        types.add("grass");
        types.add("poison");
        image_url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png";
    }

    public Pokemon(int id, String name, ArrayList<String> types, String image_url) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.image_url = image_url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    protected Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            types = new ArrayList<String>();
            in.readList(types, String.class.getClassLoader());
        } else {
            types = null;
        }
        image_url = in.readString();
        image = in.createByteArray();
        weight = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (types == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(types);
        }
        dest.writeString(image_url);
        dest.writeByteArray(image);
        dest.writeInt(weight);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}