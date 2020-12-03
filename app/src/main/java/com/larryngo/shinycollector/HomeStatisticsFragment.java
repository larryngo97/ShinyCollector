package com.larryngo.shinycollector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.larryngo.shinycollector.databinding.FragmentHomeStatisticsBinding;
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
    private FragmentHomeStatisticsBinding binding;
    public static CompletedViewModel completedViewModel;

    public static HomeStatisticsFragment newInstance() {
        return new HomeStatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeStatisticsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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

                binding.caughtNumber.setText(String.valueOf(size));

                int countSize = 0;
                for (Counter counter : counters) {
                    countSize += counter.getCount();
                }
                binding.encountersNumber.setText(String.valueOf(countSize));

                float averageSize = (float) countSize / (float) size;
                DecimalFormat df = new DecimalFormat("#.##");
                binding.encountersAvgNumber.setText(df.format(averageSize));


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
                    binding.gameWithMostName.setText(mostGamesText);

                    int mostGamesNumber = Collections.frequency(listOfGames, mostGamesText);
                    binding.gameWithMostNumber.setText(String.valueOf(mostGamesNumber));
                }

                Optional<Map.Entry<String, Long>> mostPokemonNameMap = listOfPokemon.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue());

                if(mostPokemonNameMap.isPresent()) {
                    String mostPokemonText = mostPokemonNameMap.get().getKey();
                    binding.pokemonWithMostName.setText(mostPokemonText);

                    int mostPokemonNumber = Collections.frequency(listOfPokemon, mostPokemonText);
                    binding.pokemonWithMostNumber.setText(String.valueOf(mostPokemonNumber));
                }

                Random rand = new Random();
                int randomPokemonIndex = rand.nextInt(size);
                System.out.println(randomPokemonIndex);

                Counter randomPokemonCounter = counters.get(randomPokemonIndex);
                Glide.with(requireContext())
                        .load(randomPokemonCounter.getPokemon().getImage())
                        .placeholder(R.drawable.missingno)
                        .into(binding.randomImagePokemon);

                Glide.with(requireContext())
                        .load(randomPokemonCounter.getPlatform().getImage())
                        .placeholder(R.drawable.missingno)
                        .into(binding.randomImagePlatform);

                binding.randomEncountersName.setText(String.valueOf(randomPokemonCounter.getPokemon().getNickname()));
                binding.randomEncountersNumber.setText(String.valueOf(randomPokemonCounter.getCount()));

                Optional<Counter> mostEncountersMap = counters.stream()
                        .max(Comparator.comparing(Counter::getCount));

                if(mostEncountersMap.isPresent()) {
                    Counter counterWithMostEncounters = mostEncountersMap.get();

                    Glide.with(requireContext())
                            .load(counterWithMostEncounters.getPokemon().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(binding.mostImagePokemon);

                    Glide.with(requireContext())
                            .load(counterWithMostEncounters.getPlatform().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(binding.mostImagePlatform);

                    binding.mostEncountersName.setText(counterWithMostEncounters.getPokemon().getNickname());
                    binding.mostEncountersNumber.setText(String.valueOf(counterWithMostEncounters.getCount()));
                }

                Optional<Counter> leastEncountersMap = counters.stream()
                        .min(Comparator.comparing(Counter::getCount));

                if(leastEncountersMap.isPresent()) {
                    Counter counterWithLeastEncounters = leastEncountersMap.get();

                    Glide.with(requireContext())
                            .load(counterWithLeastEncounters.getPokemon().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(binding.leastImagePokemon);

                    Glide.with(requireContext())
                            .load(counterWithLeastEncounters.getPlatform().getImage())
                            .placeholder(R.drawable.missingno)
                            .into(binding.leastImagePlatform);

                    binding.leastEncountersName.setText(counterWithLeastEncounters.getPokemon().getNickname());
                    binding.leastEncountersNumber.setText(String.valueOf(counterWithLeastEncounters.getCount()));
                }
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if(getActivity() == null) return;
        getActivity().getMenuInflater().inflate(R.menu.home_menu_var, menu);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
