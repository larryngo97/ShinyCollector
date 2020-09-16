package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Counter implements Parcelable {
    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;
    private int count;
    private int increment_count;

    public Counter(Game game, Pokemon pokemon, Platform platform, Method method, int count, int increment) {
        this.game = game;
        this.pokemon = pokemon;
        this.platform = platform;
        this.method = method;
        this.count = count;
        this.increment_count = increment;
    }

    public void add(int num)
    {
        count += num;
    }

    public int getCount() {
        return count;
    }

    public int getIncrement_count() {
        return increment_count;
    }

    public Game getGame() {
        return game;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Method getMethod() {
        return method;
    }

    protected Counter(Parcel in) {
        game = (Game) in.readParcelable(Game.class.getClassLoader());
        pokemon = (Pokemon) in.readParcelable(Pokemon.class.getClassLoader());
        platform = (Platform) in.readParcelable(Platform.class.getClassLoader());
        method = (Method) in.readParcelable(Method.class.getClassLoader());
        count = in.readInt();
        increment_count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(game, flags);
        dest.writeParcelable(pokemon, flags);
        dest.writeParcelable(platform, flags);
        dest.writeParcelable(method, flags);
        dest.writeInt(count);
        dest.writeInt(increment_count);
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