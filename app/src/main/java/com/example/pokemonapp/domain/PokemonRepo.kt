package com.example.pokemonapp.domain

import com.example.pokemonapp.core.network.NetworkResult
import com.example.pokemonapp.data.modelresponse.pokemon.PokemonResponse
import com.example.pokemonapp.domain.model.detail.Pokemon
import com.example.pokemonapp.domain.model.list.PokemonList

interface PokemonRepo {
    suspend fun getPokemonList (limit:Int, offset:Int): NetworkResult<PokemonList>

    suspend fun getPokemonInfo (name: String): NetworkResult<Pokemon>
}