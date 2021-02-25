package com.jesse.pokemon.utils

import android.view.View
import com.jesse.pokemon.data.PokemonResults

interface ItemViewClickListener {
    fun onClickLister(position:Int, view: View, pokemonResults: PokemonResults)
}