package com.example.pokemonapp.domain.model.list

data class PokemonList(
    val count: Int = 0,
    val next: String ="",
    val resultResponses: List<ResultUI> = emptyList()
    // val previous: Any
)