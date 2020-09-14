package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Counter implements Parcelable {
    private int id;
    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;
    private int count;

    public Counter(Game game, Pokemon pokemon, Platform platform, Method method) {
        this.game = game;
        this.pokemon = pokemon;
        this.platform = platform;
        this.method = method;
        this.count = 0;
    }

    public Counter(Game game, Pokemon pokemon, Platform platform, Method method, int count) {
        this.game = game;
        this.pokemon = pokemon;
        this.platform = platform;
        this.method = method;
        this.count = count;
    }

    public void add(int num)
    {
        count += num;
    }

    protected Counter(Parcel in) {
        id = in.readInt();
        game = (Game) in.readValue(Game.class.getClassLoader());
        pokemon = (Pokemon) in.readValue(Pokemon.class.getClassLoader());
        platform = (Platform) in.readValue(Platform.class.getClassLoader());
        method = (Method) in.readValue(Method.class.getClassLoader());
        count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(game);
        dest.writeValue(pokemon);
        dest.writeValue(platform);
        dest.writeValue(method);
        dest.writeInt(count);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Counter> CREATOR = new Parcelable.Creator<Counter>() {
        @Override
        public Counter createFromParcel(Parcel in) {
            return new Counter(in);
        }

        @Override
        public Counter[] newArray(int size) {
            return new Counter[size];
        }
    };
}