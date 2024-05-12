package com.example.pokemonapp.presentation

import com.example.pokemonapp.domain.model.list.ResultUI


sealed class SideEffects  {
    data class ErrorEffect(val err: String): SideEffects()
    data class ExceptionEffect(val throwable: Throwable): SideEffects()
    data class ClickEffect(val pokemon: ResultUI.Result): SideEffects()
}
