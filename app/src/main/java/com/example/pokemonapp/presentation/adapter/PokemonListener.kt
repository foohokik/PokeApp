package com.example.pokemonapp.presentation.adapter

import com.example.pokemonapp.domain.model.list.ResultUI


interface PokemonListener {
    fun onClickArticle (pokemon: ResultUI.Result)
}