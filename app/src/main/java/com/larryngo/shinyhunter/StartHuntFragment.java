package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;


import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.HomeHuntingFragment.huntingViewModel;
import static com.larryngo.shinyhunter.StartHuntActivity.fm;

/*
    OVERVIEW

    This fragment contains all the information that is about to be used for the hunt.
    When clicking a button, it will ask for user inputs that will update here once completed.
    *See other fragments to see how data is collected
 */
public class StartHuntFragment extends Fragment {
    private Button button_game;
    private Button button_pokemon;
    private Button button_platform;
    private Button button_method;
    private GifImageView image_pokemon;
    private ImageView image_platform;
    private Button button_start;

    private View view;
    private Game game;
    private Pokemon pokemon;
    private Platform platform;
    private Method method;

    public void updateGame(Game input){
        if(input == null) return;
        game = input;
        button_game.setText(game.getName());

        //Enables the pokemon button
        button_pokemon.setEnabled(true);
    }

    public void updatePokemon(Pokemon input) {
        if(input == null) return;
        pokemon = input;

        //Changes the image of the platform preview.
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            button_pokemon.setText(pokemon.getName());

            Glide.with(view.getContext())
                    .load(pokemon.getImage())
                    .placeholder(R.drawable.missingno)
                    .into(image_pokemon);
        });

        //Enables the platform button
        button_platform.setEnabled(true);
    }

    public void updatePlatform(Platform input) {
        if(input == null) return;
        platform = input;
        button_platform.setText(platform.getName());

        //Changes the image of the pokemon preview.
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> Glide.with(view.getContext())
                .load(platform.getImage())
                .placeholder(R.drawable.missingno)
                .into(image_platform));

        //Enables the method button
        button_method.setEnabled(true);
    }

    public void updateMethod(Method input) {
        if(input == null) return;
        method = input;
        button_method.setText(method.getName());

        //Enables the start button
        button_start.setVisibility(View.VISIBLE);
        button_start.setEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_starthunt, container, false);
            button_game = view.findViewById(R.id.starthunt_button_selectgame);
            button_pokemon = view.findViewById(R.id.starthunt_button_selectpokemon);
            button_platform = view.findViewById(R.id.starthunt_button_selectplatform);
            button_method = view.findViewById(R.id.starthunt_button_selectmethod);
            image_pokemon = view.findViewById(R.id.starthunt_image_pokemon);
            image_platform = view.findViewById(R.id.starthunt_image_platform);
            button_start = view.findViewById(R.id.starthunt_button_start);

            //Setting buttons to be disabled until other options are selected. (See update
            //functions above)
            /*
            button_game.setEnabled(true);
            button_pokemon.setEnabled(false);
            button_platform.setEnabled(false);
            button_method.setEnabled(false);
            button_start.setEnabled(false);

             */
            button_start.setVisibility(View.INVISIBLE);

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

                  button_start.setText(R.string.button_resume);
                  button_start.setOnClickListener(v -> {
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
                button_start.setOnClickListener(view -> {
                    Counter counter = new Counter(game, pokemon, platform, method, 0, 1); //starts out as 0 count, with 1 as increment.
                    Date date = new Date();
                    String stringDate = DateFormat.getDateTimeInstance().format(date);
                    counter.setDateCreated(stringDate);

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
            button_game.setOnClickListener(v -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new GameListFragment()).addToBackStack(null).commit());

            //POKEMON BUTTON
            //Main entry in the set. Creates a view of the entire pokemon library where user can
            //select the icon from specific games (or choose default!) for creativity.
            button_pokemon.setOnClickListener(v -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new PokemonListFragment()).addToBackStack(null).commit());


            //PLATFORM BUTTON
            //Creates a platform image that sits under the pokemon. Just a cosmetic enhancement!
            button_platform.setOnClickListener(view -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new PlatformListFragment()).addToBackStack(null).commit());

            //METHOD BUTTON
            //A simple list of available methods.
            button_method.setOnClickListener(view -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new MethodListFragment()).addToBackStack(null).commit());

        }
        return view;
    }
}
