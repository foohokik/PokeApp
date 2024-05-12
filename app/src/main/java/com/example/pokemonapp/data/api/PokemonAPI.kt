package com.example.pokemonapp.data.api

import com.example.pokemonapp.core.network.NetworkResult
import com.example.pokemonapp.data.modelresponse.pokemon.PokemonResponse
import com.example.pokemonapp.data.modelresponse.pokemonlist.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): NetworkResult<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): NetworkResult<PokemonResponse>
}