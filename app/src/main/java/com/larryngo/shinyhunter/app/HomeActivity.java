package com.larryngo.shinyhunter.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.larryngo.shinyhunter.HomeCompletedFragment;
import com.larryngo.shinyhunter.HomeHuntingFragment;
import com.larryngo.shinyhunter.HomeStatisticsFragment;
import com.larryngo.shinyhunter.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SectionsPageAdapter viewPagerAdapter;

    public SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.home_tabLayout);
        viewPager = findViewById(R.id.home_viewPager);

        HomeHuntingFragment homeHuntingFragment = new HomeHuntingFragment();
        HomeCompletedFragment homeCompletedFragment = new HomeCompletedFragment();
        HomeStatisticsFragment homeStatisticsFragment = new HomeStatisticsFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeHuntingFragment, getResources().getString(R.string.home_hunting_title));
        viewPagerAdapter.addFragment(homeCompletedFragment, getResources().getString(R.string.home_completed_title));
        viewPagerAdapter.addFragment(homeStatisticsFragment, getResources().getString(R.string.home_statistics_title));
        viewPager.setAdapter(viewPagerAdapter);

        if(tabLayout.getTabAt(0) != null) {
            tabLayout.getTabAt(0).setIcon(R.drawable.icon_hunting);
        }
        if(tabLayout.getTabAt(1) != null) {
            tabLayout.getTabAt(1).setIcon(R.drawable.icon_completed);
        }
        if(tabLayout.getTabAt(2) != null) {
            tabLayout.getTabAt(2).setIcon(R.drawable.icon_statistics);
        }


    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();
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
    public void onBackPressed() {
        super.onBackPressed();
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
