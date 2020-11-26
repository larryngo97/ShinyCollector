package com.larryngo.shinycollector.viewmodels;

import android.app.Application;

import com.larryngo.shinycollector.models.Counter;
import com.larryngo.shinycollector.respositories.HuntingRepository;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.CompletableObserver;
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

    public LiveData<Counter> getCounter(int id) {
        return repository.getCounter(id);
    }

    public void addCounter(final Counter entry) {
        List<Counter> currentList = counters.getValue();
        if(currentList != null) {
            repository.addCounter(entry)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onComplete() {
                            /*
                            //Automatically goes to hunting right after adding a counter
                            if(currentList.get(size - 1) != null) {
                                int lastId = currentList.get(size - 1).getId();
                                entry.setId(lastId + 1);
                                PokemonHuntActivity.start(activity, entry);
                            }

                             */

                        }

                        @Override
                        public void onError(@androidx.annotation.NonNull Throwable e) {

                        }

                        @Override
                        public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

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
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

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
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

                    }
                });
    }

    public void modifyGame(Counter counter) {
        repository.modifyGame(counter.getId(), counter.getGame())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {
                        System.out.println("Modified Game for ID: " + counter.getId() + " to " + counter.getGame().getName());
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

                    }
                });
    }

    public void modifyPokemon(Counter counter) {
        repository.modifyPokemon(counter.getId(), counter.getPokemon())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {
                        System.out.println("Modified Pokemon for ID: " + counter.getId() + " to " + counter.getPokemon().getName());
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

                    }
                });
    }

    public void modifyPlatform(Counter counter) {
        repository.modifyPlatform(counter.getId(), counter.getPlatform())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {
                        System.out.println("Modified Platform for ID: " + counter.getId() + " to " + counter.getPlatform().getName());
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

                    }
                });
    }

    public void modifyMethod(Counter counter) {
        repository.modifyMethod(counter.getId(), counter.getMethod())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onComplete() {
                        System.out.println("Modified Method for ID: " + counter.getId() + " to " + counter.getMethod().getName());
                    }

                    @Override
                    public void onError(@androidx.annotation.NonNull Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@androidx.annotation.NonNull Disposable d) {

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
