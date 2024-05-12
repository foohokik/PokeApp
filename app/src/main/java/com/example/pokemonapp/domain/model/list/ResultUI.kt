package com.example.pokemonapp.domain.model.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ResultUI {

    @Parcelize
    data class Result(
        val name: String,
        val url: String,
        var color: Int
    ) : Parcelable, ResultUI()

    object Loading : ResultUI()
}
