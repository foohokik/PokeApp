package com.example.pokemonapp.data.modelresponse.pokemonlist

data class ResultResponse(
    val name: String,
    val url: String
)  {

    fun getImageUrl(): String {
        val index = url.split("/").takeLast(2).first()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${index}.png"
    }

}

