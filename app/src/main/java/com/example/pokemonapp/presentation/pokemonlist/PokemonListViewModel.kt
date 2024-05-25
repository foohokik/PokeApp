package com.example.pokemonapp.presentation.pokemonlist

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.core.network.onError
import com.example.pokemonapp.core.network.onException
import com.example.pokemonapp.core.network.onSuccess
import com.example.pokemonapp.di.IoDispatcher
import com.example.pokemonapp.domain.PokemonRepo
import com.example.pokemonapp.domain.model.list.PokemonList
import com.example.pokemonapp.domain.model.list.ResultUI
import com.example.pokemonapp.presentation.SideEffects
import com.example.pokemonapp.presentation.adapter.PokemonListener
import com.example.pokemonapp.presentation.pagination.PaginationListener
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
class PokemonListViewModel @Inject
constructor(
    private val repository: PokemonRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), PokemonListener, PaginationListener {

    private val _pokemonFlow = MutableStateFlow(PokemonList())
    val pokemonFlow = _pokemonFlow.asStateFlow()

    private val _stateProgressBar = MutableStateFlow(true)
    val stateProgressBar = _stateProgressBar.asStateFlow()

    private val _sideEffects = Channel<SideEffects>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var skip = 0
    private var isLoading = false

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch { _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
    }

    init {
        getPokemonList()
    }


    private fun getPokemonList() {
        viewModelScope.launch(exceptionHandler) {
            withContext(ioDispatcher) {
                isLoading = true
                if (skip >= LIMIT) {
                    _pokemonFlow.update {
                        it.copy(resultResponses = pokemonFlow.value.resultResponses + ResultUI.Loading)
                    }
                } else {
                    _stateProgressBar.value = isLoading
                }
                val result = repository.getPokemonList(LIMIT, skip)
                result
                    .onSuccess { pokemons ->
                        _pokemonFlow.value =
                            pokemons.copy(resultResponses = pokemonFlow.value.resultResponses + pokemons.resultResponses)
                        if (skip < pokemons.count) {
                            skip += LIMIT
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

                _pokemonFlow.update {
                    it.copy(resultResponses = pokemonFlow.value.resultResponses.filterIsInstance<ResultUI.Result>())
                }

                isLoading = false
                _stateProgressBar.value = isLoading
            }
        }
    }

    override fun onClickArticle(pokemon: ResultUI.Result, view: ImageView) {
        viewModelScope.launch { _sideEffects.send(SideEffects.ClickEffect(pokemon, view)) }
    }

    override fun isLoading(): Boolean {
        return isLoading
    }

    override fun isLastItems(): Boolean {
        return _pokemonFlow.value.count <= skip
    }

    override fun loadNextItems(isError: Boolean) {
        getPokemonList()
    }

    companion object {
        private const val LIMIT = 20
    }

}