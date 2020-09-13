package com.larryngo.shinyhunter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

import com.larryngo.shinyhunter.PokeAPI.PokeAPIService;
import com.larryngo.shinyhunter.models.PokemonList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class PokemonListFragment extends Fragment {
    private final String TAG = "ShinyHunter";

    private Retrofit retrofit;
    private PokeAPIService service;

    private View view;
    private SearchView searchbar;
    private RecyclerView rv;

    private ArrayList<PokemonList> pokemonList = new ArrayList<>();
    private PokemonListAdapter adapter;
    private int totalPokemonLimit = 890;

    private ProgressDialog progressDialog;
    private PokemonListAdapter.PokemonListListener listener;

    private SendPokemonToView sendPokemonToView;
    public interface SendPokemonToView {
        void sendPokemonToView(PokemonList pokemonData);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.pokemon_list_layout, container, false);
            rv = view.findViewById(R.id.pokemon_list_recycler);

            setOnClickListener();
            adapter = new PokemonListAdapter(this.getContext(), pokemonList, listener);
            adapter.setItemCount(totalPokemonLimit);
            rv.setHasFixedSize(true);
            rv.setAdapter(adapter);

            final GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
            rv.setLayoutManager(layoutManager);

            retrofit = new Retrofit.Builder()
                    .baseUrl(PokeAPIService.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(PokeAPIService.class);
            obtainData();
        }
        return view;
    }

    public void setOnClickListener() {
        listener = new PokemonListAdapter.PokemonListListener() {
            @Override
            public void onClick(View v, int position) {
                sendPokemonToView.sendPokemonToView(pokemonList.get(position));
            }
        };
    }

    public void obtainData() {
        Call<PokemonList> pokemonListCall = service.obtainPokemonList(totalPokemonLimit, 0);

        pokemonListCall.enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                if (response.isSuccessful()) {
                    PokemonList pokemonRequest = response.body();
                    pokemonList = pokemonRequest.getResults();

                    adapter.addDataList(pokemonList);

                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendPokemonToView = (SendPokemonToView) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in receiving data!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}