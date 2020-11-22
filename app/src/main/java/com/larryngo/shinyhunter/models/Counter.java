package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.larryngo.shinyhunter.PokemonHuntActivity.MAX_COUNT_VALUE;

@Entity(tableName = "counters")
public class Counter implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String dateCreated;
    private String dateFinished;

    private int count;
    private int step;

    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;

    public Counter(Game game, Pokemon pokemon, Platform platform, Method method, int count, int step) {
        this.game = game;
        this.pokemon = pokemon;
        this.platform = platform;
        this.method = method;
        this.count = count;
        this.step = step;
    }

    public Counter(Counter counter) {
        this.id = counter.id;
        this.dateCreated = counter.dateCreated;
        this.dateFinished = counter.dateFinished;
        this.count = counter.count;
        this.step = counter.step;
        this.game = counter.game;
        this.pokemon = counter.pokemon;
        this.platform = counter.platform;
        this.method = counter.method;
    }

    public static Comparator<Counter> COMPARE_BY_LISTID_DESC = (obj1, obj2) ->
            obj2.id - obj1.id;

    public static Comparator<Counter> COMPARE_BY_LISTID_ASC = (obj1, obj2) ->
            obj1.id - obj2.id;

    public static Comparator<Counter> COMPARE_BY_GAME_DESC = (obj1, obj2) ->
            obj2.getGame().getId() - obj1.getGame().getId();

    public static Comparator<Counter> COMPARE_BY_GAME_ASC = (obj1, obj2) ->
            obj1.getGame().getId()- obj2.getGame().getId();

    public static Comparator<Counter> COMPARE_BY_POKEMONID_DESC = (obj1, obj2) ->
            obj2.getPokemon().getId() - obj1.getPokemon().getId();

    public static Comparator<Counter> COMPARE_BY_POKEMONID_ASC = (obj1, obj2) ->
            obj1.getPokemon().getId() - obj2.getPokemon().getId();

    public static Comparator<Counter> COMPARE_BY_COUNT_DESC = (obj1, obj2) ->
            obj2.count - obj1.count;

    public static Comparator<Counter> COMPARE_BY_COUNT_ASC = (obj1, obj2) ->
            obj1.count - obj2.count;

    public static Comparator<Counter> COMPARE_BY_NICKNAME_DESC = (obj1, obj2) ->
            obj2.getPokemon().getNickname().compareTo(obj1.getPokemon().getNickname());

    public static Comparator<Counter> COMPARE_BY_NICKNAME_ASC = (obj1, obj2) ->
            obj1.getPokemon().getNickname().compareTo(obj2.getPokemon().getNickname());

    public void add(int num)
    {
        count += num;
        if(count < 0)
            count = 0;
        else if (count > MAX_COUNT_VALUE)
            count = MAX_COUNT_VALUE;
    }

    public String timeElapsed() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a", Locale.US);
        if(dateCreated != null) {
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            //get current date in case somehow there is no data
            Date date1 = new Date();
            Date date2 = new Date();

            try {
                date1 = simpleDateFormat.parse(dateCreated); //get start date
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(dateFinished != null) {
                try {
                    date2 = simpleDateFormat.parse(dateFinished); //get captured date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            long difference = date2.getTime() - date1.getTime(); // calculates time elapsed in milliseconds

            //conversions to D,H,M,S
            long days = difference / daysInMilli;
            difference = difference % daysInMilli;

            long hours = difference / hoursInMilli;
            difference = difference % hoursInMilli;

            long minutes = difference / minutesInMilli;
            difference = difference % minutesInMilli;

            long seconds = difference / secondsInMilli;


            return days + "d " + hours + "h " + minutes + "m " + seconds + "s ";
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
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

    protected Counter(Parcel in) {
        game = in.readParcelable(Game.class.getClassLoader());
        pokemon = in.readParcelable(Pokemon.class.getClassLoader());
        platform = in.readParcelable(Platform.class.getClassLoader());
        method = in.readParcelable(Method.class.getClassLoader());
        dateCreated = in.readString();
        dateFinished = in.readString();
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
        dest.writeString(dateCreated);
        dest.writeString(dateFinished);
        dest.writeInt(count);
        dest.writeInt(step);
    }

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