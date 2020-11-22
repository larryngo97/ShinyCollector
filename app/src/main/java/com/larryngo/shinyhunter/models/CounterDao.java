package com.larryngo.shinyhunter.models;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;

@Dao
public abstract class CounterDao {

    @Query("DELETE FROM counters")
    public abstract Completable deleteAll();

    @Delete
    public abstract Completable deleteCounter(Counter counter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insert(Counter counter);

    @Query("SELECT * FROM counters ORDER BY id")
    public abstract LiveData<List<Counter>> loadAllCounters();

    @Query("SELECT COUNT(*) FROM counters")
    public abstract int count();

    @Query("SELECT * FROM counters WHERE id = :counterId")
    public abstract LiveData<Counter> loadCounter(int counterId);

    @Query("UPDATE counters " + "SET game = :entry_game WHERE id = :counterId")
    public abstract Completable modifyGame(int counterId, Game entry_game);

    @Query("UPDATE counters " + "SET pokemon = :entry_pokemon WHERE id = :counterId")
    public abstract Completable modifyPokemon(int counterId, Pokemon entry_pokemon);

    @Query("UPDATE counters " + "SET platform = :entry_platform WHERE id = :counterId")
    public abstract Completable modifyPlatform(int counterId, Platform entry_platform);

    @Query("UPDATE counters " + "SET method = :entry_method WHERE id = :counterId")
    public abstract Completable modifyMethod(int counterId, Method entry_method);


    @Query("UPDATE counters " + "SET count = :value WHERE id = :counterId")
    public abstract Completable modifyCount(int counterId, int value);

    @Query("UPDATE counters " + "SET step = :step WHERE id = :counterId")
    public abstract Completable modifyStep(int counterId, int step);

    @Query("UPDATE counters " + "SET count = 0")
    public abstract Completable resetValues();

}
