package com.example.pokemonapp.presentation.pagination

interface PaginationListener {
  fun isLoading(): Boolean

  fun isLastItems(): Boolean

  fun loadNextItems(isError: Boolean = false)
}
