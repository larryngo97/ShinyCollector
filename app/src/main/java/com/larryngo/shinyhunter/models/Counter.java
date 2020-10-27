package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "counters")
public class Counter implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;
    private String nickname;
    private int count;
    private int step;
    private int position;

    public Counter(Game game, Pokemon pokemon, Platform platform, Method method, int count, int step) {
        this.game = game;
        this.pokemon = pokemon;
        this.platform = platform;
        this.method = method;
        this.count = count;
        this.step = step;
        this.nickname = pokemon.getName();
    }

    public Counter(Counter counter) {
        this.id = counter.id;
        this.game = counter.game;
        this.pokemon = counter.pokemon;
        this.platform = counter.platform;
        this.method = counter.method;
        this.count = counter.count;
        this.step = counter.step;
        this.position = counter.position;
        this.nickname = counter.getPokemon().getName();
    }

    public void add(int num)
    {
        count += num;
        if(count < 0)
            count = 0;
        else if (count > 99999)
            count = 99999;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    protected Counter(Parcel in) {
        game = (Game) in.readParcelable(Game.class.getClassLoader());
        pokemon = (Pokemon) in.readParcelable(Pokemon.class.getClassLoader());
        platform = (Platform) in.readParcelable(Platform.class.getClassLoader());
        method = (Method) in.readParcelable(Method.class.getClassLoader());
        count = in.readInt();
        step = in.readInt();
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
        dest.writeInt(step);
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