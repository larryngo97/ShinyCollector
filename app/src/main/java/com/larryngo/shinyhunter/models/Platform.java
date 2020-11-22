package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Platform implements Parcelable {
    private String name;
    private byte[] image;

    public Platform() {
        this.name = "Grass";
        this.image = null;
    }

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

    protected Platform(Parcel in) {
        name = in.readString();
        image = in.createByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByteArray(image);
    }

    public static final Parcelable.Creator<Platform> CREATOR = new Parcelable.Creator<Platform>() {
        @Override
        public Platform createFromParcel(Parcel in) {
            return new Platform(in);
        }

        @Override
        public Platform[] newArray(int size) {
            return new Platform[size];
        }
    };
}
