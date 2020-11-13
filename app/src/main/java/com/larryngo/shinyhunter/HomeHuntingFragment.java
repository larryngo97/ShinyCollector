package com.larryngo.shinyhunter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Collections;
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
    public static HuntingViewModel huntingViewModel;

    private View view;
    private RecyclerView recyclerView;
    private ImageView recyclerMenu;
    private HuntingAdapter.HuntingListener listener;

    private TextView text_nohunts;
    private ImageView image_arrow_down;
    FloatingActionButton fab;
    FloatingActionButton fab2;


    private boolean firstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
        recyclerView = view.findViewById(R.id.home_hunting_rv);
        recyclerMenu = view.findViewById(R.id.hunting_list_options);

        text_nohunts = view.findViewById(R.id.home_text_nohunts);
        image_arrow_down = view.findViewById(R.id.home_arrow_down);
        fab = view.findViewById(R.id.home_fab);
        fab2 = view.findViewById(R.id.home_fab2);
        fab2.setVisibility(View.GONE); // testing purposes

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        huntingViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                final int size = counters.size();

                System.out.println("Size: " + size);
                if (size == 0) {
                    text_nohunts.setVisibility(View.VISIBLE);
                    image_arrow_down.setVisibility(View.VISIBLE);
                } else {
                    text_nohunts.setVisibility(View.GONE);
                    image_arrow_down.setVisibility(View.GONE);
                }

                adapter.setCountersList(counters);

                counters.sort(Counter.COMPARE_BY_LISTID_DESC); //ALWAYS sort by the newest entry, followed by preference
                counters.sort(Counter.COMPARE_BY_GAME_DESC);

                recyclerView.setAdapter(adapter);
            }
        });

        /*
        if(firstLoad) {
            firstLoad = false;
            recyclerView.post(() -> recyclerView.setAdapter(adapter));
        }

        */

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Options");
        getActivity().getMenuInflater().inflate(R.menu.hunting_entry, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hunting_menu_delete) {
            Toast.makeText(getContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
