package com.example.pokemonapp.domain.model.detail


data class Pokemon(
    val height: Int = 0,
    val name: String = "",
    val stats: List<Stat> = emptyList(),
    val types: List<Type> = emptyList(),
    val weight: Int = 0,
    val url: String? = null,
    val color: Int? = null
)
