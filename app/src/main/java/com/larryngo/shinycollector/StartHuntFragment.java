package com.larryngo.shinycollector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.larryngo.shinycollector.databinding.FragmentStarthuntBinding;
import com.larryngo.shinycollector.models.Counter;
import com.larryngo.shinycollector.models.Game;
import com.larryngo.shinycollector.models.Method;
import com.larryngo.shinycollector.models.Platform;
import com.larryngo.shinycollector.models.Pokemon;


import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import static com.larryngo.shinycollector.HomeHuntingFragment.huntingViewModel;
import static com.larryngo.shinycollector.StartHuntActivity.fm;

/*
    OVERVIEW

    This fragment contains all the information that is about to be used for the hunt.
    When clicking a button, it will ask for user inputs that will update here once completed.
    *See other fragments to see how data is collected
 */
public class StartHuntFragment extends Fragment {
    private FragmentStarthuntBinding binding;
    private View view;
    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;

    private InterstitialAd ad;

    public void updateGame(Game input){
        if(input == null) return;
        game = input;
        binding.buttonSelectgame.setText(game.getName());

        //Enables the pokemon button
        binding.buttonSelectpokemon.setEnabled(true);
    }

    public void updatePokemon(Pokemon input) {
        if(input == null) return;
        pokemon = input;

        //Changes the image of the platform preview.
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            binding.buttonSelectpokemon.setText(pokemon.getName());

            Glide.with(requireContext())
                    .load(pokemon.getImage())
                    .placeholder(R.drawable.missingno)
                    .into(binding.imagePokemon);
        });

        //Enables the platform button
        binding.buttonSelectplatform.setEnabled(true);
    }

    public void updatePlatform(Platform input) {
        if(input == null) return;
        platform = input;
        binding.buttonSelectplatform.setText(platform.getName());

        //Changes the image of the pokemon preview.
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> Glide.with(requireContext())
                .load(platform.getImage())
                .placeholder(R.drawable.missingno)
                .into(binding.imagePlatform));

        //Enables the method button
        binding.buttonSelectmethod.setEnabled(true);
    }

    public void updateMethod(Method input) {
        if(input == null) return;
        method = input;
        binding.buttonSelectmethod.setText(method.getName());

        //Enables the start button
        binding.buttonStart.setVisibility(View.VISIBLE);
        binding.buttonStart.setEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            binding = FragmentStarthuntBinding.inflate(inflater, container, false);
            view = binding.getRoot();

            ad = new InterstitialAd(requireContext());
            ad.setAdUnitId(getString(R.string.admob_interstitial_starthunt_id));
            ad.loadAd(new AdRequest.Builder().build());

            //Setting buttons to be disabled until other options are selected. (See update
            //functions above)
            binding.buttonSelectgame.setEnabled(true);
            binding.buttonSelectpokemon.setEnabled(false);
            binding.buttonSelectplatform.setEnabled(false);
            binding.buttonSelectmethod.setEnabled(false);
            binding.buttonStart.setEnabled(false);

            binding.buttonStart.setVisibility(View.INVISIBLE);

            Bundle extras = getArguments();
            if(extras != null) {
                int active_hunt_id = getArguments().getInt("ARGUMENT_ACTIVE_HUNT");
                LiveData<Counter> counterLiveData = huntingViewModel.getCounter(active_hunt_id);
                counterLiveData.observe(getViewLifecycleOwner(), counter -> {
                    game = counter.getGame();
                    pokemon = counter.getPokemon();
                    platform = counter.getPlatform();
                    method = counter.getMethod();

                    updateGame(game);
                    updatePokemon(pokemon);
                    updatePlatform(platform);
                    updateMethod(method);

                    binding.buttonStart.setText(R.string.button_resume);
                    binding.buttonStart.setOnClickListener(v -> {
                        Counter modifiedCounter = new Counter(game, pokemon, platform, method, counter.getCount(), counter.getStep());
                        modifiedCounter.setId(active_hunt_id);

                        huntingViewModel.modifyGame(modifiedCounter);
                        huntingViewModel.modifyPokemon(modifiedCounter);
                        huntingViewModel.modifyPlatform(modifiedCounter);
                        huntingViewModel.modifyMethod(modifiedCounter);

                        PokemonHuntActivity.start(getActivity(), modifiedCounter);

                        if(getActivity() == null) return;
                        getActivity().finish();
                    });
                });
            } else {
                //START BUTTON
                //Packages up the entire content of this fragment and pushes them towards the next activity.
                //This will also create a new data entry onto the current hunts list so the user can come
                //back to it any time.
                binding.buttonStart.setOnClickListener(v -> {
                    Counter counter = new Counter(game, pokemon, platform, method, 0, 1); //starts out as 0 count, with 1 as increment.
                    Date date = new Date();
                    String stringDate = DateFormat.getDateTimeInstance().format(date);
                    counter.setDateCreated(stringDate);

                    if(ad.isLoaded() || ad.isLoading()) {
                        ad.show();
                    }

                    huntingViewModel.addCounter(counter);

                    //Prevents the user from going back to the startup screen. When a hunt has been created
                    //it should bring the user back to the home page (where they can see their current hunts)
                    if(getActivity() == null) return;
                    getActivity().finish();
                });
            }

            //GAME BUTTON
            //Selecting a game which will filter out which pokemon can be caught (it determines
            //the current generation of that game)
            binding.buttonSelectgame.setOnClickListener(v -> fm.beginTransaction()
                    .replace(R.id.starthunt_fragment_container, new GameListFragment()).addToBackStack(null).commit());

            //POKEMON BUTTON
            //Main entry in the set. Creates a view of the entire pokemon library where user can
            //select the icon from specific games (or choose default!) for creativity.
            binding.buttonSelectpokemon.setOnClickListener(v -> fm.beginTransaction()
                    .replace(R.id.starthunt_fragment_container, new PokemonListFragment()).addToBackStack(null).commit());


            //PLATFORM BUTTON
            //Creates a platform image that sits under the pokemon. Just a cosmetic enhancement!
            binding.buttonSelectplatform.setOnClickListener(v -> fm.beginTransaction()
                    .replace(R.id.starthunt_fragment_container, new PlatformListFragment()).addToBackStack(null).commit());

            //METHOD BUTTON
            //A simple list of available methods.
            binding.buttonSelectmethod.setOnClickListener(v -> fm.beginTransaction()
                    .replace(R.id.starthunt_fragment_container, new MethodListFragment()).addToBackStack(null).commit());
        }
        return view;
    }
}
