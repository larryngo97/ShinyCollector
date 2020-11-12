package com.larryngo.shinyhunter.app;

import android.app.Application;

import com.larryngo.shinyhunter.database.DatabaseHolder;
import com.larryngo.shinyhunter.database.TinyDB;

public class App extends Application {
    private static TinyDB tinyDB;
    private static App instance;

    public static TinyDB getTinyDB() {
        if (tinyDB == null) {
            tinyDB = new TinyDB(App.getInstance());
        }
        return tinyDB;
    }
    public static App getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        App.instance = this;
        DatabaseHolder.init(this);
    }
}