package com.larryngo.shinycollector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.larryngo.shinycollector.models.Counter;
import com.larryngo.shinycollector.viewmodels.CompletedViewModel;
import com.larryngo.shinycollector.viewmodels.CompletedViewModelFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeStatisticsFragment extends Fragment {
    public static CompletedViewModel completedViewModel;
    private View view;

    public static HomeStatisticsFragment newInstance() {
        return new HomeStatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_statistics, container, false);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CompletedViewModelFactory factory = new CompletedViewModelFactory(requireActivity().getApplication());
        completedViewModel = new ViewModelProvider(this, factory).get(CompletedViewModel.class);

        completedViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if(counters != null) {
                int size = counters.size();
                if(size == 0) return;

                ((TextView)view.findViewById(R.id.caught_number)).setText(String.valueOf(size));

                int countSize = 0;
                for (Counter counter : counters) {
                    countSize += counter.getCount();
                }
                ((TextView)view.findViewById(R.id.encounters_number)).setText(String.valueOf(countSize));

                float averageSize = (float) countSize / (float) size;
                DecimalFormat df = new DecimalFormat("#.##");
                ((TextView)view.findViewById(R.id.encounters_avg_number)).setText(df.format(averageSize));


                List<String> listOfGames = new ArrayList<>();
                List<String> listOfPokemon = new ArrayList<>();

                for (Counter counter : counters) {
                    listOfGames.add(counter.getGame().getName());
                    listOfPokemon.add(counter.getPokemon().getName());
                }

                Optional<Map.Entry<String, Long>> mostGamesNameMap = listOfGames.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue());

                if(mostGamesNameMap.isPresent()) {
                    String mostGamesText = mostGamesNameMap.get().getKey();
                    ((TextView)view.findViewById(R.id.game_with_most_name)).setText(mostGamesText);

                    int mostGamesNumber = Collections.frequency(listOfGames, mostGamesText);
                    ((TextView)view.findViewById(R.id.game_with_most_number)).setText(String.valueOf(mostGamesNumber));
                }

                Optional<Map.Entry<String, Long>> mostPokemonNameMap = listOfPokemon.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue());

                if(mostPokemonNameMap.isPresent()) {
                    String mostPokemonText = mostPokemonNameMap.get().getKey();
                    ((TextView)view.findViewById(R.id.pokemon_with_most_name)).setText(mostPokemonText);

                    int mostPokemonNumber = Collections.frequency(listOfPokemon, mostPokemonText);
                    ((TextView)view.findViewById(R.id.pokemon_with_most_number)).setText(String.valueOf(mostPokemonNumber));
                }

                Random rand = new Random();
                int randomPokemonIndex = rand.nextInt(size - 1);
                System.out.println(randomPokemonIndex);

                Counter randomPokemonCounter = counters.get(randomPokemonIndex);
                Glide.with(requireContext())
                        .load(randomPokemonCounter.getPokemon().getImage())
                        .placeholder(R.drawable.missingno)
                        .into(((ImageView)view.findViewById(R.id.random_image_pokemon)));

                Glide.with(requireContext())
                        .load(randomPokemonCounter.getPlatform().getImage())
                        .placeholder(R.drawable.missingno)
                        .into(((ImageView)view.findViewById(R.id.random_image_platform)));

                ((TextView)view.findViewById(R.id.random_encounters_number)).setText(String.valueOf(randomPokemonCounter.getCount()));
                ((TextView)view.findViewById(R.id.random_encounters_name)).setText(String.valueOf(randomPokemonCounter.getPokemon().getNickname()));

                Optional<Counter> mostEncountersMap = counters.stream()
                        .max(Comparator.comparing(Counter::getCount));

                if(mostEncountersMap.isPresent()) {
                    Counter counterWithMostEncounters = mostEncountersMap.get();

                    Glide.with(requireContext())
                            .load(counterWithMostEncounters.getPokemon().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(((ImageView)view.findViewById(R.id.most_image_pokemon)));

                    Glide.with(requireContext())
                            .load(counterWithMostEncounters.getPlatform().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(((ImageView)view.findViewById(R.id.most_image_platform)));

                    ((TextView)view.findViewById(R.id.most_encounters_number)).setText(String.valueOf(counterWithMostEncounters.getCount()));
                    ((TextView)view.findViewById(R.id.most_encounters_name)).setText(counterWithMostEncounters.getPokemon().getNickname());
                }

                Optional<Counter> leastEncountersMap = counters.stream()
                        .min(Comparator.comparing(Counter::getCount));

                if(leastEncountersMap.isPresent()) {
                    Counter counterWithLeastEncounters = leastEncountersMap.get();

                    Glide.with(requireContext())
                            .load(counterWithLeastEncounters.getPokemon().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(((ImageView)view.findViewById(R.id.least_image_pokemon)));

                    Glide.with(requireContext())
                            .load(counterWithLeastEncounters.getPlatform().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(((ImageView)view.findViewById(R.id.least_image_platform)));

                    ((TextView)view.findViewById(R.id.least_encounters_number)).setText(String.valueOf(counterWithLeastEncounters.getCount()));
                    ((TextView)view.findViewById(R.id.least_encounters_name)).setText(counterWithLeastEncounters.getPokemon().getNickname());
                }
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if(getActivity() == null) return;
        getActivity().getMenuInflater().inflate(R.menu.home_menu_var, menu);

    }
}
