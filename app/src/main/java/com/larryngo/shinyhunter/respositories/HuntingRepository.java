package com.larryngo.shinyhunter.respositories;

import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.CounterDao;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import io.reactivex.Completable;

public class HuntingRepository  {
    @NonNull
    private final CounterDao counterDao;

    public HuntingRepository(@NonNull CounterDao counterDao) {
        this.counterDao = counterDao;
    }

    public Completable addCounter(Counter counter) {
        return counterDao.insert(counter);
    }

    public Completable deleteCounter(Counter counter) {
        return counterDao.deleteCounter(counter);
    }

    public Completable deleteAll() {
        return counterDao.deleteAll();
    }

    public LiveData<List<Counter>> getCounters() {
        return counterDao.loadAllCounters();
    }

    public LiveData<Counter> getCounter(int id) {
        return counterDao.loadCounter(id);
    }

    public Completable modifyGame(int id, Game game) {
        return counterDao.modifyGame(id, game);
    }

    public Completable modifyPokemon(int id, Pokemon pokemon) {
        return counterDao.modifyPokemon(id, pokemon);
    }

    public Completable modifyPlatform (int id, Platform platform) {
        return counterDao.modifyPlatform(id, platform);
    }

    public Completable modifyMethod(int id, Method method) {
        return counterDao.modifyMethod(id, method);
    }

    public Completable setCount(int id, int value) {
        return counterDao.modifyCount(id, value);
    }

    public Completable setStep(int id, int value) { return counterDao.modifyStep(id, value); }

    /*
    private static HuntingRepository instance;
    private ArrayList<Counter> dataSet = new ArrayList<>();

    public static HuntingRepository getInstance() {
        if(instance == null) {
            instance = new HuntingRepository();
        } return instance;
    }


    public MutableLiveData<List<Counter>> getHuntingList() {
        setHuntingList();

        MutableLiveData<List<Counter>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setHuntingList() {

    }

     */
}
