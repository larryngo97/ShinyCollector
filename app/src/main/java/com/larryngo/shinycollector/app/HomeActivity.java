package com.larryngo.shinycollector.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.larryngo.shinycollector.HomeCompletedFragment;
import com.larryngo.shinycollector.HomeHuntingFragment;
import com.larryngo.shinycollector.HomeStatisticsFragment;
import com.larryngo.shinycollector.R;
import com.larryngo.shinycollector.util.Settings;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import com.larryngo.shinycollector.databinding.ActivityMainBinding;


public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean nightMode = Settings.isDarkModeOn();
        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.HomeThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.HomeTheme);
        }
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        setupViewPager();
    }

    public void setupViewPager() {
        HomeHuntingFragment homeHuntingFragment = HomeHuntingFragment.newInstance();
        HomeCompletedFragment homeCompletedFragment = HomeCompletedFragment.newInstance();
        HomeStatisticsFragment homeStatisticsFragment = HomeStatisticsFragment.newInstance();

        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager);

        SectionsPageAdapter viewPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(homeHuntingFragment, getResources().getString(R.string.home_hunting_title));
        viewPagerAdapter.addFragment(homeCompletedFragment, getResources().getString(R.string.home_completed_title));
        viewPagerAdapter.addFragment(homeStatisticsFragment, getResources().getString(R.string.home_statistics_title));
        binding.homeViewPager.setAdapter(viewPagerAdapter);

        if(binding.homeTabLayout.getTabAt(0) != null) {
            binding.homeTabLayout.getTabAt(0).setIcon(R.drawable.icon_hunting);
        }
        if(binding.homeTabLayout.getTabAt(1) != null) {
            binding.homeTabLayout.getTabAt(1).setIcon(R.drawable.icon_completed);
        }
        if(binding.homeTabLayout.getTabAt(2) != null) {
            binding.homeTabLayout.getTabAt(2).setIcon(R.drawable.icon_statistics);
        }

    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();
        public SectionsPageAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment (Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home_menu_settings) {
            Intent intent = new Intent(HomeActivity.this, Settings.class);
            startActivity(intent); //home should be reset every time user exits settings

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch(key) {
            case Settings.SETTING_SORT_KEY:
                System.out.println("Sort Key changed");
                break;
            case Settings.SETTING_NIGHTMODE_KEY:
                System.out.println("Dark Mode changed");
                break;
            default:

        }
    }
}
