package com.example.pokemonapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonapp.domain.model.list.ResultUI

class PokemonDiffUtil(
    private val oldList: MutableList<ResultUI>,
    private val newList: List<ResultUI>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is ResultUI.Result && newItem is ResultUI.Result) {
            oldItem.name == newItem.name
        } else {
            oldItem.javaClass == newItem.javaClass
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
