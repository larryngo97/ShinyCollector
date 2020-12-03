package com.larryngo.shinycollector;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.larryngo.shinycollector.adapters.HomeListAdapter;
import com.larryngo.shinycollector.databinding.FragmentHomeGridBinding;
import com.larryngo.shinycollector.util.Utilities;
import com.larryngo.shinycollector.viewmodels.HuntingViewModel;
import com.larryngo.shinycollector.viewmodels.HuntingViewModelFactory;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeHuntingFragment extends Fragment {
    private FragmentHomeGridBinding binding;
    public HomeListAdapter adapter;
    public static HuntingViewModel huntingViewModel;
    private HomeListAdapter.RecyclerViewListener recyclerViewListener;

    private int oldListSize;

    public static HomeHuntingFragment newInstance() {
        return new HomeHuntingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeGridBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.homeFab.setImageResource(R.drawable.icon_plus);

        setOnClickListener();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HuntingViewModelFactory factory = new HuntingViewModelFactory(requireActivity().getApplication());
        huntingViewModel = new ViewModelProvider(this, factory).get(HuntingViewModel.class);
        adapter = new HomeListAdapter(this.getContext(), recyclerViewListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.homeRecyclerview.setLayoutManager(layoutManager);

        huntingViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                final int size = counters.size();

                if (size == 0) {
                    binding.homeTextNohunts.setVisibility(View.VISIBLE);
                    binding.homeArrowDown.setVisibility(View.VISIBLE);
                } else {
                    binding.homeTextNohunts.setVisibility(View.GONE);
                    binding.homeArrowDown.setVisibility(View.GONE);
                }

                if(oldListSize != size) {
                    System.out.println("Old size: " + oldListSize + " \nNew Size: " + size);
                    adapter.notifyDataSetChanged();
                }

                adapter.setCountersList(counters);

                binding.homeRecyclerview.setAdapter(adapter);
                oldListSize = size;
            }
        });
    }

    public void setOnClickListener() {
        binding.homeFab.setOnClickListener(v -> {
            Intent intent = new Intent (getContext(), StartHuntActivity.class);
            startActivity(intent);

        });

        recyclerViewListener = (v, position) -> PokemonHuntActivity.start(getActivity(), Objects.requireNonNull(huntingViewModel.getCounters().getValue()).get(position));
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
    public void onStart() {
        super.onStart();
        adapter.refreshList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
