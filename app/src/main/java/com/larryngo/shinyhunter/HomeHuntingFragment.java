package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larryngo.shinyhunter.adapters.HomeListAdapter;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModel;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.os.Looper.getMainLooper;

public class HomeHuntingFragment extends Fragment {
    protected static ArrayList<Counter> list = new ArrayList<>();
    public HomeListAdapter adapter;
    public static HuntingViewModel huntingViewModel;

    private int oldListSize;
    private RecyclerView recyclerView;
    private HomeListAdapter.RecyclerViewListener listener;

    private TextView text_nohunts;
    private ImageView image_arrow_down;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_grid, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerview);

        text_nohunts = view.findViewById(R.id.home_text_nohunts);
        image_arrow_down = view.findViewById(R.id.home_arrow_down);
        fab = view.findViewById(R.id.home_fab);
        fab.setImageResource(R.drawable.icon_plus);

        setOnClickListener();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HuntingViewModelFactory factory = new HuntingViewModelFactory(requireActivity().getApplication());
        huntingViewModel = new ViewModelProvider(this, factory).get(HuntingViewModel.class);
        adapter = new HomeListAdapter(this.getContext(), listener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        huntingViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                final int size = counters.size();

                if (size == 0) {
                    text_nohunts.setVisibility(View.VISIBLE);
                    image_arrow_down.setVisibility(View.VISIBLE);
                } else {
                    text_nohunts.setVisibility(View.GONE);
                    image_arrow_down.setVisibility(View.GONE);
                }

                if(oldListSize != size) {
                    System.out.println("Old size: " + oldListSize + " \nNew Size: " + size);
                    adapter.notifyDataSetChanged();
                }


                adapter.setCountersList(counters);

                counters.sort(Counter.COMPARE_BY_LISTID_DESC); //ALWAYS sort by the newest entry, followed by preference
                //counters.sort(Counter.COMPARE_BY_GAME_DESC);

                recyclerView.setAdapter(adapter);
                oldListSize = size;
            }
        });
    }

    public void setOnClickListener() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent (getContext(), StartHuntActivity.class);
            startActivity(intent);

        });

        listener = (v, position) -> PokemonHuntActivity.start(getActivity(), Objects.requireNonNull(huntingViewModel.getCounters().getValue()).get(position));
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if(getActivity() == null) return;
        getActivity().getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.home_menu_search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.home_menu_search).getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

}
