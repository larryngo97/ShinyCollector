package com.larryngo.shinyhunter.viewmodels;

import android.app.Activity;
import android.app.Application;

import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.respositories.CompletedRepository;
import com.larryngo.shinyhunter.respositories.HuntingRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CompletedViewModel extends AndroidViewModel {
    private final CompletedRepository repository;
    private final LiveData<List<Counter>> counters;

    public CompletedViewModel(Application application, CompletedRepository completedRepository) {
        super(application);
        repository = completedRepository;
        counters = repository.getCounters();
    }

    public LiveData<List<Counter>> getCounters() {
        return counters;
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
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
