package com.larryngo.shinyhunter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.larryngo.shinyhunter.models.Game;
import com.larryngo.shinyhunter.models.Method;
import com.larryngo.shinyhunter.models.Platform;
import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.models.PokemonList;

public class StartHuntActivity extends AppCompatActivity implements
        GameListFragment.FragmentGameListener,
        PokemonViewFragment.FragmentPokemonViewListener,
        PokemonListFragment.SendPokemonToView,
        PlatformListFragment.FragmentPlatformListListener,
        MethodListFragment.FragmentMethodListListener {

    protected static FragmentManager fm;
    protected StartHuntFragment startHuntFragment;
    protected GameListFragment gameListFragment;
    protected PokemonListFragment pokemonListFragment;
    protected PokemonViewFragment pokemonViewFragment;
    protected MethodListFragment methodListFragment;

    @Override
    public void sendPokemonToView(PokemonList pokemonData) {
        pokemonViewFragment.receiveIndex(pokemonData);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.starthunt_fragment_container, pokemonViewFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onInputGameSent(Game game) {
        startHuntFragment.updateGame(game);
    }

    @Override
    public void onInputPokemonSent(Pokemon pokemon) {
        startHuntFragment.updatePokemon(pokemon);
    }

    @Override
    public void onInputPlatformSent(Platform platform) {
        startHuntFragment.updatePlatform(platform);
    }

    @Override
    public void onInputMethodSent(Method method) {
        startHuntFragment.updateMethod(method);
    }

    public void startFragment(Fragment fragment)
    {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.starthunt_fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_starthunt);

        fm = getSupportFragmentManager();

        startHuntFragment = new StartHuntFragment();
        gameListFragment = new GameListFragment();
        pokemonListFragment = new PokemonListFragment();
        pokemonViewFragment = new PokemonViewFragment();
        methodListFragment = new MethodListFragment();

        startFragment(startHuntFragment);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = fm.getBackStackEntryCount(); //gets count of the back stack
        if (backStackCount != 0) //if theres a current back stack
        {
            fm.popBackStack(); //pops the stack, reformats the layout
        }
        else
        {
            super.onBackPressed();
        }
    }
}
