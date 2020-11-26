package com.larryngo.shinycollector.viewmodels;

import android.app.Application;

import com.larryngo.shinycollector.database.CompletedDatabase;
import com.larryngo.shinycollector.respositories.CompletedRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CompletedViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application app;
    private final CompletedRepository repository;

    public CompletedViewModelFactory(Application application) {
        app = application;
        repository = new CompletedRepository(CompletedDatabase.database().counterDao());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CompletedViewModel(app, repository);
    }
}
