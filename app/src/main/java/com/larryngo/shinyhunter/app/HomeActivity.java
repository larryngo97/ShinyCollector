package com.larryngo.shinyhunter.app;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.larryngo.shinyhunter.HomeBaseFragment;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.app.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.home_fragment_container, new HomeBaseFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        int backStackCount = fm.getBackStackEntryCount(); //gets count of the back stack
        if (backStackCount != 0) //if theres a current back stack
        {
            fm.popBackStack(); //pops the stack, reformats the layout
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getTinyDB().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getTinyDB().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
