package com.example.pokemonapp.data.modelresponse

import com.example.pokemonapp.core.network.NetworkResult
import com.example.pokemonapp.data.modelresponse.pokemon.PokemonResponse
import com.example.pokemonapp.data.modelresponse.pokemon.StatResponse
import com.example.pokemonapp.data.modelresponse.pokemon.TypeResponse
import com.example.pokemonapp.data.modelresponse.pokemonlist.PokemonListResponse
import com.example.pokemonapp.data.modelresponse.pokemonlist.ResultResponse
import com.example.pokemonapp.domain.model.detail.Pokemon
import com.example.pokemonapp.domain.model.detail.Stat
import com.example.pokemonapp.domain.model.detail.Type
import com.example.pokemonapp.domain.model.list.PokemonList
import com.example.pokemonapp.domain.model.list.ResultUI


fun ResultResponse.toResult(): ResultUI.Result {

    return ResultUI.Result(name, getImageUrl(), 0)

}

fun List<ResultResponse>.convertResultList(): List<ResultUI.Result> {
    return this.map { it.toResult() }
}

fun PokemonListResponse.toPokemonList(): PokemonList {
    return PokemonList(count, next, results.convertResultList())
}

fun NetworkResult<PokemonListResponse>.toNetworkResultPokemonList(): NetworkResult<PokemonList> {
    return when (this) {
        is NetworkResult.Success<PokemonListResponse> -> {
            NetworkResult.Success(this.data.toPokemonList())
        }

        is NetworkResult.Error<PokemonListResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}

fun StatResponse.toStat(): Stat {
    return Stat(base_stat, effort, stat.name)
}

fun TypeResponse.toType(): Type {
    return Type(slot, type.name)
}

fun List<StatResponse>.convertToStatList(): List<Stat> {
    return this.map { it.toStat() }
}

fun List<TypeResponse>.convertToTypeList(): List<Type> {
    return this.map { it.toType() }
}

fun PokemonResponse.convertToPokemon(): Pokemon {
    return Pokemon(height, name, stats.convertToStatList(), types.convertToTypeList(), weight)
}

fun NetworkResult<PokemonResponse>.toNetworkResultPokemon(): NetworkResult<Pokemon> {
    return when (this) {
        is NetworkResult.Success<PokemonResponse> -> {
            NetworkResult.Success(this.data.convertToPokemon())
        }

        is NetworkResult.Error<PokemonResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}


