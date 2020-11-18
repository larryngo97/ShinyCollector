package com.larryngo.shinyhunter.viewmodels;

import android.app.Application;

import com.larryngo.shinyhunter.database.HuntingDatabase;
import com.larryngo.shinyhunter.respositories.HuntingRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HuntingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application app;
    private final HuntingRepository repository;

    public HuntingViewModelFactory(Application application) {
        app = application;
        repository = new HuntingRepository(HuntingDatabase.database().counterDao());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new HuntingViewModel(app, repository);
    }
}
