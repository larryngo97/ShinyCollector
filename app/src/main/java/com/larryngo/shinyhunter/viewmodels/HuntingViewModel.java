package com.larryngo.shinyhunter.viewmodels;

import android.app.Application;
import android.widget.Toast;

import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.respositories.HuntingRepository;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HuntingViewModel extends AndroidViewModel {
    private final HuntingRepository repository;
    private final LiveData<List<Counter>> counters;

    HuntingViewModel(Application application, HuntingRepository huntingRepository) {
        super(application);
        repository = huntingRepository;
        counters = huntingRepository.getCounters();
    }

    public LiveData<List<Counter>> getCounters() {
        return counters;
    }

    public void addCounter(final Counter entry) {
        List<Counter> currentList = counters.getValue();
        if(currentList != null) {
            int size = currentList.size() + 1;
            entry.setPosition(size);
            repository.addCounter(entry);
            Toast.makeText(getApplication(), "Created new entry! Size: " + counters.getValue().size(), Toast.LENGTH_SHORT).show();
        }
    }

    public void modifyCounter(Counter counter, @IntRange(from = 0, to = 99999) int amount) {
        repository.setCount(counter.getId(), amount);
    }

    /*
    private MutableLiveData<List<Counter>> hunting_list;
    private HuntingRepository huntingRepository;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init() {
        if(hunting_list == null) {
            huntingRepository = HuntingRepository.getInstance();
            hunting_list = huntingRepository.getHuntingList();
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

     */
}
