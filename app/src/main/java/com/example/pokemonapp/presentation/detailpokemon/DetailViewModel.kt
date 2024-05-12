package com.example.pokemonapp.presentation.detailpokemon

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.core.network.onError
import com.example.pokemonapp.core.network.onException
import com.example.pokemonapp.core.network.onSuccess
import com.example.pokemonapp.di.IoDispatcher
import com.example.pokemonapp.domain.PokemonRepo
import com.example.pokemonapp.domain.model.detail.Pokemon
import com.example.pokemonapp.domain.model.list.ResultUI
import com.example.pokemonapp.presentation.SideEffects
import com.example.pokemonapp.presentation.detailpokemon.DetailFragment.Companion.ARG
import com.example.pokemonapp.presentation.detailpokemon.DetailFragment.Companion.ARG_ID
import com.example.pokemonapp.presentation.pokemonlist.PokemonListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _pokemonFlow:MutableStateFlow<Pokemon?> = MutableStateFlow (Pokemon())
    val pokemonFlow = _pokemonFlow.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val _stateProgressBar = MutableStateFlow(true)
    val stateProgressBar = _stateProgressBar.asStateFlow()

    private var pokemon: ResultUI.Result? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch { _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
    }

    init {
        pokemon = savedStateHandle[ARG_ID]
        pokemon?.let { getPokemonInfo(it) }
    }

    private fun getPokemonInfo(pokemon: ResultUI.Result) {
        viewModelScope.launch(exceptionHandler) {
            withContext(ioDispatcher) {
                val result = repository.getPokemonInfo(pokemon.name)
                result
                    .onSuccess { pokemons ->
                        _pokemonFlow.update {state ->
                            if (state == null) {
                               Pokemon(height = pokemons.height,
                                   name = pokemons.name,
                                   stats = pokemons.stats,
                                   types = pokemons.types,
                                   weight = pokemons.weight,
                                   url = pokemon.url,
                                   color = pokemon.color)
                            } else {
                                state?.copy(
                                    height = pokemons.height,
                                    name = pokemons.name,
                                    stats = pokemons.stats,
                                    types = pokemons.types,
                                    weight = pokemons.weight,
                                    url = pokemon.url,
                                    color = pokemon.color
                                )
                            }
                        }
                    }
                    .onError { _, message ->
                        _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
                    }
                    .onException { throwable ->
                        _sideEffects.send(
                            SideEffects.ExceptionEffect(
                                throwable
                            )
                        )
                    }
            }
            _stateProgressBar.value = false
        }
    }

}