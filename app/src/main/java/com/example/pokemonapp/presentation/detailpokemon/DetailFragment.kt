package com.example.pokemonapp.presentation.detailpokemon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.pokemonapp.R
import com.example.pokemonapp.core.getColorOfType
import com.example.pokemonapp.core.hide
import com.example.pokemonapp.core.setTint
import com.example.pokemonapp.core.show
import com.example.pokemonapp.databinding.FragmentDetailBinding
import com.example.pokemonapp.domain.model.detail.Pokemon
import com.example.pokemonapp.domain.model.list.ResultUI
import com.example.pokemonapp.presentation.SideEffects
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private val viewModel: DetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.pokemonFlow.collect(::setContent) }
                launch { viewModel.stateProgressBar.collect(::renderLoading) }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
            }
        }
    }

    private fun setContent(state: Pokemon) {
        with(binding) {
            tvNameDetail.text = state.name
            setTypesState(state)
            state.color?.let {
                constraint.setBackgroundColor(it)
                activity?.window?.statusBarColor = it
            }

            Glide.with(ivPok.context).load(state.url).into(ivPok)
            tvHeight.text = state.height.toString()
            tvWeight.text = state.weight.toString()
            setProgressIndicators(state)
        }
    }

    private fun setProgressIndicators(pokemon: Pokemon) {
        with(binding) {
            if (pokemon.stats.isEmpty()) {
                return
            } else {
                progressHp.labelText = pokemon.stats[0].nameStat
                progressHp.max = MAX
                progressHp.progress = pokemon.stats[0].base_stat.toFloat()
                progressAtk.labelText = pokemon.stats[1].nameStat
                progressAtk.max = MAX
                progressAtk.progress = pokemon.stats[1].base_stat.toFloat()
                progressDef.labelText = pokemon.stats[2].nameStat
                progressDef.max = MAX
                progressDef.progress = pokemon.stats[2].base_stat.toFloat()
            }
        }
    }


    private fun setTypesState(pokemon: Pokemon) {
        with(binding) {
            when (pokemon.types.size) {
                0 -> return
                1 -> {
                    tvType1.text = pokemon.types[0].nameType
                    ivType1.backgroundTintList = AppCompatResources.getColorStateList(
                        requireContext(),
                        pokemon.types[0].nameType.getColorOfType()
                    )
                    ivType2.hide()
                    tvType2.hide()
                }

                else -> {
                    ivType2.show()
                    tvType2.show()
                    tvType1.text = pokemon.types[0].nameType
                    tvType2.text = pokemon.types[1].nameType
                    ivType1.backgroundTintList = AppCompatResources.getColorStateList(
                        requireContext(),
                        pokemon.types[0].nameType.getColorOfType()
                    )
                    ivType2.backgroundTintList = AppCompatResources.getColorStateList(
                        requireContext(),
                        pokemon.types[1].nameType.getColorOfType()
                    )
                }
            }
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        if (!isLoading) {
            with(binding) {
                progressBarDetail.hide()
                ivPok.show()
            }
        }
    }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error, sideEffects.err),
                    Toast.LENGTH_LONG
                ).show()
            }

            is SideEffects.ExceptionEffect -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error, sideEffects.throwable.message),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            is SideEffects.ClickEffect -> Unit
        }
    }

    companion object {
        private const val MAX = 100f
        const val ARG_ID = "ARG_ID"
        const val ARG = "ARG"

        @JvmStatic
        fun getInstance(pokemon: ResultUI.Result): DetailFragment {
            return DetailFragment().apply { arguments = bundleOf(ARG_ID to pokemon) }
        }
    }

}