package com.larryngo.shinyhunter.viewmodels;

import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.respositories.HuntingRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HuntingViewModel extends ViewModel {
    private MutableLiveData<List<Counter>> hunting_list;
    private HuntingRepository huntingRespository;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init() {
        if(hunting_list == null) {
            huntingRespository = HuntingRepository.getInstance();
            hunting_list = huntingRespository.getHuntingList();
        }
    }

    public void addNewValue(final Counter entry) {
        isUpdating.setValue(true);

        List<Counter> currentList = hunting_list.getValue();
        currentList.add(entry);
        hunting_list.postValue(currentList);

        isUpdating.setValue(false);
    }

    public LiveData<List<Counter>> getHuntingList() {
        return hunting_list;
    }

    public LiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }
}
