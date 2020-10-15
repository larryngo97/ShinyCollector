package com.larryngo.shinyhunter.respositories;

import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class HuntingRepository {
    private static HuntingRepository instance;
    private ArrayList<Counter> dataSet = new ArrayList<>();
    private Counter dummy = new Counter(
            new Game("Pokemon Red", null, 1),
            new Pokemon(0, "Missingno", null, "null"),
            new Platform("none", null),
            new Method(1, null),
            127, 1);

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
        dataSet.add(dummy);
    }
}
