package com.larryngo.shinyhunter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class HomeBaseFragment extends Fragment {
    private ViewPager viewPager;
    private SectionsPageAdapter viewPagerAdapter;

    private HomeHuntingFragment homeHuntingFragment = new HomeHuntingFragment();
    private HomeCompletedFragment homeCompletedFragment = new HomeCompletedFragment();
    private HomeStatisticsFragment homeStatisticsFragment = new HomeStatisticsFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerAdapter = new SectionsPageAdapter(getChildFragmentManager(), getContext());
        viewPager = view.findViewById(R.id.home_viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.home_tabbar);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter {
        Context context;
        public SectionsPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new HomeHuntingFragment();
                    break;
                case 1:
                    fragment = new HomeCompletedFragment();
                    break;
                case 2:
                    fragment = new HomeStatisticsFragment();
                    break;
            }
            return fragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return context.getResources().getString(R.string.home_hunting_title);
                case 1:
                    return context.getResources().getString(R.string.home_completed_title);
                case 2:
                    return context.getResources().getString(R.string.home_statistics_title);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
