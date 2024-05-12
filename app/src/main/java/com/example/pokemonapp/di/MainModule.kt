package com.example.pokemonapp.di

import com.example.pokemonapp.data.repository.PokemonRepoImpl
import com.example.pokemonapp.domain.PokemonRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
interface MainModule {

    @Binds
    fun bindMainRepository(repository: PokemonRepoImpl): PokemonRepo
}