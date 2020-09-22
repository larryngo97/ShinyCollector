package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        viewPagerAdapter = new SectionsPageAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.home_viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.home_tabbar);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = view.findViewById(R.id.home_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent (getContext(), StartHuntActivity.class);
            startActivity(intent);

        });
        return view;
    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter {
        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
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
                    return "Hunting";
                case 1:
                    return "Completed";
                case 2:
                    return "Statistics";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
