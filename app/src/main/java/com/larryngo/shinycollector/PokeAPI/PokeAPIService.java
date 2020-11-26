package com.larryngo.shinycollector.PokeAPI;

import com.larryngo.shinycollector.models.Pokemon;
import com.larryngo.shinycollector.models.PokemonList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeAPIService {
    String baseURL = "https://pokeapi.co/api/v2/";
    String baseURL_shiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/";

    @GET("pokemon")
    Call<PokemonList> obtainPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") String id);

}
