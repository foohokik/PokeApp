package com.example.pokemonapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemonapp.R
import com.example.pokemonapp.databinding.ActivityMainBinding
import com.example.pokemonapp.presentation.pokemonlist.PokemonList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        savedInstanceState ?: initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                binding.mainContainerView.id,
                PokemonList()
            )
            .commit()
    }

}