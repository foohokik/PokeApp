package com.example.pokemonapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonapp.databinding.LoadingItemBinding
import com.example.pokemonapp.databinding.PokemonItemBinding
import com.example.pokemonapp.domain.model.list.ResultUI

class PokemonAdapter(private val listener: PokemonListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val items = mutableListOf<ResultUI>()

  override fun getItemViewType(position: Int) =
      when (items[position]) {
        is ResultUI.Result -> TYPE_ITEM
        is ResultUI.Loading -> TYPE_LOADING
        else -> throw IllegalArgumentException("Invalid type of item $position")
      }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      TYPE_ITEM -> PokemonViewHolder(PokemonItemBinding.inflate(layoutInflater, parent, false))
      TYPE_LOADING -> LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
      else -> LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
    }
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is PokemonViewHolder -> holder.bind(items[position] as ResultUI.Result, listener)
      is LoadingViewHolder -> holder.bind(items[position] as ResultUI.Loading)
    }
  }

  fun setItems(newItems: List<ResultUI>) {
    val diffResult = DiffUtil.calculateDiff(PokemonDiffUtil(items, newItems))
    items.clear()
    items.addAll(newItems)
    diffResult.dispatchUpdatesTo(this)
  }

  companion object {
    private const val TYPE_ITEM = 0
    private const val TYPE_LOADING = 1
  }
}
