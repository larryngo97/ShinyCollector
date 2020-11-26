package com.larryngo.shinycollector.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.larryngo.shinycollector.models.Game;
import com.larryngo.shinycollector.models.Method;
import com.larryngo.shinycollector.models.Platform;
import com.larryngo.shinycollector.models.Pokemon;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class ObjectTypeConverters {
    Gson gson = new Gson();

    @TypeConverter
    public String gameToString(Game game) {
        if(game == null) {
            return null;
        }

        Type type = new TypeToken<Game>() {}.getType();
        return gson.toJson(game, type);
    }

    @TypeConverter
    public Game stringToGame(String data) {
        if(data == null) {
            return null;
        }

        Type typeList = new TypeToken<Game>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String pokemonToString(Pokemon pokemon) {
        if(pokemon == null) {
            return null;
        }

        Type type = new TypeToken<Pokemon>() {}.getType();
        return gson.toJson(pokemon, type);
    }

    @TypeConverter
    public Pokemon stringToPokemon(String data) {
        if(data == null) {
            return null;
        }

        Type typeList = new TypeToken<Pokemon>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String platformToString(Platform platform) {
        if(platform == null) {
            return null;
        }

        Type type = new TypeToken<Platform>() {}.getType();
        return gson.toJson(platform, type);
    }

    @TypeConverter
    public Platform stringToPlatform(String data) {
        if(data == null) {
            return null;
        }

        Type typeList = new TypeToken<Platform>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String methodToString(Method method) {
        if(method == null) {
            return null;
        }

        Type type = new TypeToken<Method>() {}.getType();
        return gson.toJson(method, type);
    }

    @TypeConverter
    public Method stringToMethod(String data) {
        if(data == null) {
            return null;
        }

        Type typeList = new TypeToken<Method>() {}.getType();
        return gson.fromJson(data, typeList);
    }
}
