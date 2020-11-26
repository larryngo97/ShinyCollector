package com.larryngo.shinycollector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larryngo.shinycollector.adapters.HomeListAdapter;
import com.larryngo.shinycollector.util.Utilities;
import com.larryngo.shinycollector.viewmodels.CompletedViewModel;
import com.larryngo.shinycollector.viewmodels.CompletedViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCompletedFragment extends Fragment {
    public HomeListAdapter adapter;
    public static CompletedViewModel completedViewModel;
    private HomeListAdapter.RecyclerViewListener recyclerViewListener;

    private RecyclerView recyclerView;
    FloatingActionButton fab;

    private int oldListSize;

    public static HomeCompletedFragment newInstance() {
        return new HomeCompletedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_grid, container, false);
        recyclerView = view.findViewById(R.id.home_recyclerview);
        fab = view.findViewById(R.id.home_fab);
        fab.setImageResource(R.drawable.icon_completed);
        fab.setVisibility(View.GONE);

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
                int size = counters.size();

                if(oldListSize != size) {
                    System.out.println("Old size: " + oldListSize + " \nNew Size: " + size);
                    adapter.notifyDataSetChanged();
                }

                adapter.setCountersList(counters);

                recyclerView.setAdapter(adapter);
                oldListSize = size;
            }
        });

    }

    public void setOnClickListener() {
        recyclerViewListener = (v, position) -> adapter.popupDetails(position);
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

        searchView.setOnCloseListener(() -> {
            adapter.refreshList();
            return false;
        });

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Utilities.closeKeyboard(getActivity());
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                adapter.refreshList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.refreshList();
    }
}
