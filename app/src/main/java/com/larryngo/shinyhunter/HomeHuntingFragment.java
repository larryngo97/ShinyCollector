package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larryngo.shinyhunter.adapters.HuntingAdapter;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModel;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeHuntingFragment extends Fragment {
    protected static ArrayList<Counter> list = new ArrayList<>();
    protected HuntingAdapter adapter;
    protected static HuntingViewModel huntingViewModel;

    private View view;
    private RecyclerView recyclerView;
    private HuntingAdapter.HuntingListener listener;

    FloatingActionButton fab;
    FloatingActionButton fab2;


    private boolean firstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
        recyclerView = view.findViewById(R.id.home_hunting_rv);

        fab = view.findViewById(R.id.home_fab);
        fab2 = view.findViewById(R.id.home_fab2);

        setOnClickListener();

        return view;
    }

    public void setOnClickListener() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent (getContext(), StartHuntActivity.class);
            startActivity(intent);

        });

        fab2.setOnClickListener(v -> {
            Game game = new Game();
            Pokemon pokemon = new Pokemon();
            Platform platform = new Platform();
            Method method = new Method();
            Counter counter = new Counter(game, pokemon, platform, method, 0, 1);
            huntingViewModel.addCounter(getActivity(), counter);
        });

        listener = new HuntingAdapter.HuntingListener() {
            @Override
            public void onClick(View v, int position) throws ExecutionException, InterruptedException {
                PokemonHuntActivity.start(getActivity(), Objects.requireNonNull(huntingViewModel.getCounters().getValue()).get(position));
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HuntingViewModelFactory factory = new HuntingViewModelFactory(requireActivity().getApplication());
        huntingViewModel = new ViewModelProvider(this, factory).get(HuntingViewModel.class);
        adapter = new HuntingAdapter(this.getContext(), listener);
        huntingViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            adapter.setCountersList(counters);

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true); //reverse the order from newest to oldest
            layoutManager.setStackFromEnd(true); //list starts from the top
            recyclerView.setLayoutManager(layoutManager);

        /*
            if(counters != null) {
                final int size = counters.size();

                System.out.println("Size: " + size);
                if (size == 0) {

                } else {
                    adapter.setCountersList(counters);
                }
            }

         */

        });

        /*
        if(firstLoad) {
            firstLoad = false;
            recyclerView.post(() -> recyclerView.setAdapter(adapter));
        }

        */

    }
}
