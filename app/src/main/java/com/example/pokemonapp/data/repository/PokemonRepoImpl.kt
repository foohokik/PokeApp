package com.example.pokemonapp.data.repository

import android.util.Log
import com.example.pokemonapp.core.network.NetworkResult
import com.example.pokemonapp.core.network.onSuccess
import com.example.pokemonapp.data.api.PokemonAPI
import com.example.pokemonapp.data.modelresponse.toNetworkResultPokemon
import com.example.pokemonapp.data.modelresponse.toNetworkResultPokemonList
import com.example.pokemonapp.domain.PokemonRepo
import com.example.pokemonapp.domain.model.detail.Pokemon
import com.example.pokemonapp.domain.model.list.PokemonList
import javax.inject.Inject

class PokemonRepoImpl @Inject constructor(
    private val api: PokemonAPI
    ) : PokemonRepo {


    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): NetworkResult<PokemonList> {
        val result =  api.getPokemonList(limit, offset)
        return result.toNetworkResultPokemonList()
    }

    override suspend fun getPokemonInfo(name: String): NetworkResult<Pokemon> {
        val result = api.getPokemonInfo(name)
        Log.d("PPPP", "result = " + result.onSuccess { Log.d("PPPP", "result == " +it) })
        return result.toNetworkResultPokemon()
    }


}