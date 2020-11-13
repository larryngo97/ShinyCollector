package com.larryngo.shinyhunter.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.larryngo.shinyhunter.PokemonHuntActivity;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.respositories.HuntingRepository;

import java.util.Collections;
import java.util.List;
import java.util.Observable;

import androidx.annotation.IntRange;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    public void addCounter(Activity activity, final Counter entry) {
        List<Counter> currentList = counters.getValue();
        if(currentList != null) {
            int size = currentList.size();
            repository.addCounter(entry)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onComplete() {
                            //int lastId = currentList.get(size - 1).getId();
                            //entry.setId(lastId + 1);
                            //PokemonHuntActivity.start(activity, entry);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }


                    });
        }
    }

    public void deleteCounter(final Counter entry) {
        repository.deleteCounter(entry)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void modifyCounter(Counter counter, @IntRange(from = 0, to = 99999) int amount) {
        System.out.println("Changing counter ID: " + counter.getId());
        System.out.println("Current Count: " + counter.getCount());
        repository.setCount(counter.getId(), amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    public void modifyStep(Counter counter, @IntRange(from = 0, to = 99) int amount) {
        repository.setStep(counter.getId(), amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {
                        System.out.println("Step: " + counter.getStep());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
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
