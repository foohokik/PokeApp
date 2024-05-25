package com.example.pokemonapp.presentation.pokemonlist

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.FragmentPokemonListBinding
import com.example.pokemonapp.presentation.SideEffects
import com.example.pokemonapp.presentation.adapter.ItemDecorator
import com.example.pokemonapp.presentation.adapter.PokemonAdapter
import com.example.pokemonapp.presentation.detailpokemon.DetailFragment
import com.example.pokemonapp.presentation.pagination.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonList : Fragment() {
    private var _binding: FragmentPokemonListBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private lateinit var pokemonAdapter: PokemonAdapter
    private val viewModel: PokemonListViewModel by viewModels()
    private var onScrollListener: OnScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
        postponeEnterTransition()
        binding.rvPokemons.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.pokemonFlow.collect { pokemonAdapter.setItems(it.resultResponses) } }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.stateProgressBar.collect(::renderLoading) }
            }
        }
    }

    private fun initViews() =
        with(binding.rvPokemons) {
            pokemonAdapter = PokemonAdapter(viewModel)
            val manager = LinearLayoutManager(requireContext())
            layoutManager = manager
            adapter = pokemonAdapter
            addItemDecoration(ItemDecorator(20, 20))
            itemAnimator = null
            onScrollListener = OnScrollListener(viewModel, manager)
            onScrollListener?.let { listener -> addOnScrollListener(listener) }
        }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {
                Toast.makeText(requireContext(), getString(R.string.error, sideEffects.err), Toast.LENGTH_LONG).show()
            }
            is SideEffects.ExceptionEffect -> {
                Toast.makeText(
                    requireContext(), getString(R.string.error, sideEffects.throwable.message), Toast.LENGTH_LONG )
                    .show()
            }
            is SideEffects.ClickEffect -> {
                sideEffects.imageView?.let {
                    parentFragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .addSharedElement(
                            it,
                            sideEffects.pokemon.name
                        )
                        .addToBackStack(null)
                        .replace(R.id.main_container_view, DetailFragment.getInstance(sideEffects.pokemon, ViewCompat.getTransitionName(sideEffects.imageView).toString() ))
                        .commit()
                }
            }
        }

    }

    private fun renderLoading (isLoading: Boolean) {
            with(binding){
                progressBar.isVisible = isLoading
                rvPokemons.isVisible = isLoading.not()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onScrollListener?.let { binding.rvPokemons.removeOnScrollListener(it) }
        _binding = null
    }

}