package com.example.pokemonapp.data.modelresponse.pokemon

data class PokemonResponse(
    val base_experience: Int,
    val height: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val stats: List<StatResponse>,
    val types: List<TypeResponse>,
    val weight: Int
)