package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import com.larryngo.shinyhunter.PokeAPI.PokeAPIService;
import com.larryngo.shinyhunter.adapters.PokemonListAdapter;
import com.larryngo.shinyhunter.models.PokemonList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.larryngo.shinyhunter.StartHuntActivity.fm;


public class PokemonListFragment extends Fragment {
    private final String TAG = "ShinyHunter";

    private Retrofit retrofit;
    private PokeAPIService service;

    private View view;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private ArrayList<PokemonList> pokemonList = new ArrayList<>();
    private PokemonListAdapter adapter;
    private int totalPokemonLimit = 892;

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
            recyclerView = view.findViewById(R.id.pokemon_list_recycler);
            searchView = view.findViewById(R.id.pokemon_list_search);

            init();
            adapter = new PokemonListAdapter(this.getContext(), pokemonList, listener);
            adapter.setItemCount(totalPokemonLimit);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            final GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
            recyclerView.setLayoutManager(layoutManager);

            retrofit = new Retrofit.Builder()
                    .baseUrl(PokeAPIService.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(PokeAPIService.class);

            obtainData();

        }
        return view;
    }

    public void init() {
        listener = (v, position) -> {
            sendPokemonToView.sendPokemonToView(adapter.getList().get(position)); //sends the platform to the main hunt menu
            closeKeyboard();
        };

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    public void obtainData() {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        loadingDialog.setMessage(R.string.pokeapi_loadingdata);

        new Thread(() -> {
            Call<PokemonList> pokemonListCall = service.obtainPokemonList(totalPokemonLimit, 0);

            pokemonListCall.enqueue(new Callback<PokemonList>() {
                @Override
                public void onResponse(@NonNull Call<PokemonList> call, @NonNull Response<PokemonList> response) {
                    if (response.isSuccessful()) {
                        PokemonList pokemonRequest = response.body();

                        if(pokemonRequest != null) {
                            pokemonList = pokemonRequest.getResults();
                            adapter.addDataList(pokemonList);
                        }

                    } else {
                        Log.e(TAG, "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PokemonList> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), R.string.pokeapi_loadingdata_failure, Toast.LENGTH_SHORT).show();
                }
            });

            if(getActivity() == null) return;
            getActivity().runOnUiThread(loadingDialog::dismissDialog);
        }).start();
    }

    public void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            View keyboardView = getActivity().getCurrentFocus();
            if(keyboardView != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
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