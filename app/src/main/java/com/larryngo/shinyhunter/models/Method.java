package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Method implements Parcelable {
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

    protected Method(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            list_methods_names = new ArrayList<String>();
            in.readList(list_methods_names, String.class.getClassLoader());
        } else {
            list_methods_names = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (list_methods_names == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list_methods_names);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Method> CREATOR = new Parcelable.Creator<Method>() {
        @Override
        public Method createFromParcel(Parcel in) {
            return new Method(in);
        }

        @Override
        public Method[] newArray(int size) {
            return new Method[size];
        }
    };
}