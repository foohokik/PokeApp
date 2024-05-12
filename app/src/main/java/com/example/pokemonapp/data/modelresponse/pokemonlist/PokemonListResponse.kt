package com.example.pokemonapp.data.modelresponse.pokemonlist

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ResultResponse>
)