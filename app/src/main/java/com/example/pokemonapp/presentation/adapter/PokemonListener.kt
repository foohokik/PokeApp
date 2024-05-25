package com.example.pokemonapp.presentation.adapter

import android.widget.ImageView
import com.example.pokemonapp.domain.model.list.ResultUI


interface PokemonListener {
    fun onClickArticle (pokemon: ResultUI.Result, view: ImageView)
}