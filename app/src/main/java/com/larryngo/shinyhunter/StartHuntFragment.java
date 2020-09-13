package com.larryngo.shinyhunter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;


import pl.droidsonroids.gif.GifImageView;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class StartHuntFragment extends Fragment {
    protected Button button_game;
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
        game = input;
        button_game.setText(game.getName());

        button_pokemon.setEnabled(true);
    }

    public void updatePokemon(Pokemon input) {
        pokemon = input;
        button_pokemon.setText(pokemon.getName());

        Glide.with(view.getContext())
                .load(pokemon.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(image_pokemon);

        button_platform.setEnabled(true);
    }

    public void updatePlatform(Platform input) {
        platform = input;
        button_platform.setText(platform.getName());

        Glide.with(view.getContext())
                .load(platform.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.missingno)
                .into(image_platform);

        button_method.setEnabled(true);
    }

    public void updateMethod(Method input) {
        method = input;
        button_method.setText(method.getName());

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

            button_game.setEnabled(true);
            button_pokemon.setEnabled(false);
            button_platform.setEnabled(false);
            button_method.setEnabled(false);
            button_start.setEnabled(false);

            button_game.setOnClickListener(v -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new GameListFragment()).addToBackStack(null).commit());

            button_pokemon.setOnClickListener(v -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new PokemonListFragment()).addToBackStack(null).commit());

            button_platform.setOnClickListener(view -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new PlatformListFragment()).addToBackStack(null).commit());

            button_method.setOnClickListener(view -> fm.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                    .replace(R.id.starthunt_fragment_container, new MethodListFragment()).addToBackStack(null).commit());

            button_start.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), PokemonHuntActivity.class);
                startActivity(intent);
            });
        }
        return view;
    }
}
