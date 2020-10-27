package com.larryngo.shinyhunter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

public class ObjectTypeConverters {
    Gson gson = new Gson();

    @TypeConverter
    public String gameToString(Game game) {
        return gson.toJson(game);
    }

    @TypeConverter
    public Game stringToGame(String data) {
        Type typeList = new TypeToken<List<Game>>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String pokemonToString(Pokemon pokemon) {
        return gson.toJson(pokemon);
    }

    @TypeConverter
    public Pokemon stringToPokemon(String data) {
        Type typeList = new TypeToken<List<Pokemon>>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String platformToString(Platform platform) {
        return gson.toJson(platform);
    }

    @TypeConverter
    public Platform stringToPlatform(String data) {
        Type typeList = new TypeToken<List<Platform>>() {}.getType();
        return gson.fromJson(data, typeList);
    }

    @TypeConverter
    public String methodToString(Method method) {
        return gson.toJson(method);
    }

    @TypeConverter
    public Method stringToMethod(String data) {
        Type typeList = new TypeToken<List<Method>>() {}.getType();
        return gson.fromJson(data, typeList);
    }
}
