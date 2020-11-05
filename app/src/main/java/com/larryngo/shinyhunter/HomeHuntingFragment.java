package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larryngo.shinyhunter.adapters.HomeHuntingAdapter;
import com.larryngo.shinyhunter.adapters.HuntingAdapter;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModel;
import com.larryngo.shinyhunter.viewmodels.HuntingViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

public class HomeHuntingFragment extends Fragment {
    protected static ArrayList<Counter> list = new ArrayList<>();
    protected HuntingAdapter adapter;
    private HuntingViewModel huntingViewModel;

    private View view;
    private RecyclerView rv;

    private boolean firstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_home_hunting_grid_layout, container, false);
            rv = view.findViewById(R.id.home_hunting_rv);

            FloatingActionButton fab = view.findViewById(R.id.home_fab);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent (getContext(), StartHuntActivity.class);
                startActivity(intent);

            });

            FloatingActionButton fab2 = view.findViewById(R.id.home_fab2);
            fab2.setOnClickListener(v -> {
                Game game = new Game();
                Pokemon pokemon = new Pokemon();
                Platform platform = new Platform();
                Method method = new Method();
                Counter counter = new Counter(game, pokemon, platform, method, 0, 1);
                huntingViewModel.addCounter(counter);

            });
            /*
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), PokemonHuntActivity.class);
                    Counter counter = adapter.getCounterObject(i);
                    intent.putExtra("counter", counter);

                    startActivity(intent);
                }
            });

             */

            /*
            huntingViewModel.init();
            huntingViewModel.getHuntingList().observe(getViewLifecycleOwner(), new Observer<List<Counter>>() {
                @Override
                public void onChanged(List<Counter> counters) {
                    adapter.notifyDataSetChanged();
                }
            });
            huntingViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean) {
                        System.out.println("Adding entry...");
                    }
                    else {
                        System.out.println("Finished adding entry!");
                    }
                }
            });

             */
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HuntingViewModelFactory factory = new HuntingViewModelFactory(requireActivity().getApplication());
        huntingViewModel = new ViewModelProvider(this, factory).get(HuntingViewModel.class);
        adapter = new HuntingAdapter(this.getContext());
        huntingViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            Game game = new Game();
            Pokemon pokemon = new Pokemon();
            Platform platform = new Platform();
            Method method = new Method();
            Counter counter = new Counter(game, pokemon, platform, method, 0, 1);
            huntingViewModel.addCounter(counter);

            if(counters != null) {
                final int size = counters.size();

                System.out.println("Size: " + size);
                if (size == 0) {

                } else {
                    adapter.setCountersList(counters);
                }
            }

        });

        if(firstLoad) {
            firstLoad = false;
            rv.post(() -> rv.setAdapter(adapter));
        }
    }
}
