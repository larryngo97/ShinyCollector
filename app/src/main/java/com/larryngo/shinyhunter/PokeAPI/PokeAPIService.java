package com.larryngo.shinyhunter.PokeAPI;

import com.larryngo.shinyhunter.models.Pokemon;
import com.larryngo.shinyhunter.models.PokemonList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeAPIService {
    String baseURL = "https://pokeapi.co/api/v2/";

    @GET("pokemon")
    Call<PokemonList> obtainPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") String id);

}