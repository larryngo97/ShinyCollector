package com.larryngo.shinyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.larryngo.shinyhunter.ObjectTypeConverters;
import com.larryngo.shinyhunter.PokemonHuntActivity;

import java.util.Comparator;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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

    public static Comparator<Counter> COMPARE_BY_LISTID_DESC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj2.id- obj1.id;
        }
    };

    public static Comparator<Counter> COMPARE_BY_GAME_DESC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj2.getGame().getId()- obj1.getGame().getId();
        }
    };

    public static Comparator<Counter> COMPARE_BY_GAME_ASC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj1.getGame().getId()- obj2.getGame().getId();
        }
    };

    public static Comparator<Counter> COMPARE_BY_POKEMONID_DESC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj2.getPokemon().getId() - obj1.getPokemon().getId();
        }
    };

    public static Comparator<Counter> COMPARE_BY_POKEMONID_ASC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj1.getPokemon().getId() - obj2.getPokemon().getId();
        }
    };

    public static Comparator<Counter> COMPARE_BY_COUNT_DESC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj2.count - obj1.count;
        }
    };

    public static Comparator<Counter> COMPARE_BY_COUNT_ASC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj1.count - obj2.count;
        }
    };

    public static Comparator<Counter> COMPARE_BY_NICKNAME_DESC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj2.getPokemon().getNickname().compareTo(obj1.getPokemon().getNickname());
        }
    };

    public static Comparator<Counter> COMPARE_BY_NICKNAME_ASC = new Comparator<Counter>() {
        public int compare(Counter obj1, Counter obj2) {
            return obj1.getPokemon().getNickname().compareTo(obj2.getPokemon().getNickname());
        }
    };

    public void add(int num)
    {
        count += num;
        if(count < 0)
            count = 0;
        else if (count > MAX_COUNT_VALUE)
            count = MAX_COUNT_VALUE;
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
        game = (Game) in.readParcelable(Game.class.getClassLoader());
        pokemon = (Pokemon) in.readParcelable(Pokemon.class.getClassLoader());
        platform = (Platform) in.readParcelable(Platform.class.getClassLoader());
        method = (Method) in.readParcelable(Method.class.getClassLoader());
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