package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larryngo.shinyhunter.adapters.HomeListAdapter;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.viewmodels.CompletedViewModel;
import com.larryngo.shinyhunter.viewmodels.CompletedViewModelFactory;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCompletedFragment extends Fragment {
    public static CompletedViewModel completedViewModel;
    public HomeListAdapter adapter;
    private HomeListAdapter.RecyclerViewListener recyclerViewListener;

    private RecyclerView recyclerView;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_grid, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerview);
        fab = view.findViewById(R.id.home_fab);
        fab.setImageResource(R.drawable.icon_completed);

        setOnClickListener();

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CompletedViewModelFactory factory = new CompletedViewModelFactory(requireActivity().getApplication());
        completedViewModel = new ViewModelProvider(this, factory).get(CompletedViewModel.class);
        adapter = new HomeListAdapter(this.getContext(), recyclerViewListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        completedViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                adapter.setCountersList(counters);

                counters.sort(Counter.COMPARE_BY_LISTID_DESC); //ALWAYS sort by the newest entry, followed by preference

                recyclerView.setAdapter(adapter);
            }
        });

    }

    public void setOnClickListener() {
        recyclerViewListener = new HomeListAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View v, int position) throws ExecutionException, InterruptedException {
                adapter.popupDetails(position);
            }
        };
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
