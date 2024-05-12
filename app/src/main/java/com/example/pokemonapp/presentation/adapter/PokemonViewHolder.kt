package com.example.pokemonapp.presentation.adapter


import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.PokemonItemBinding
import com.example.pokemonapp.domain.model.list.ResultUI


class PokemonViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(pokemon: ResultUI.Result, listener: PokemonListener) =
        with(binding) {
            var dominantColor: Int = 0
            val circularProgressDrawable = CircularProgressDrawable(ivPokemon.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(ivPokemon.context)
                .load(pokemon.url)
                .placeholder(circularProgressDrawable)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val drawable = resource as BitmapDrawable
                        val bitmap = drawable.bitmap
                        Palette.Builder(bitmap).generate {
                            it?.let { palette ->
                                dominantColor = palette.getDominantColor(
                                    ContextCompat.getColor(
                                        root.context,
                                        R.color.white
                                    )
                                )
                                pokemon.color = dominantColor
                                root.setCardBackgroundColor(
                                    dominantColor
                                )

                            }
                        }
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(ivPokemon)
            pokemonNameTextView.text = pokemon.name
            root.setOnClickListener { listener.onClickArticle(pokemon) }
        }

}
