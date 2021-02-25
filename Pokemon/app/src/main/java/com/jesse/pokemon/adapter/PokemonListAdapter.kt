package com.jesse.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jesse.pokemon.R
import com.jesse.pokemon.data.PokemonResults
import com.jesse.pokemon.data.getId
import com.jesse.pokemon.utils.ItemViewClickListener
import com.jesse.pokemon.utils.bindImage

class PokemonListAdapter (val itemViewClickListener: ItemViewClickListener)
    : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>(){

    var pokemonList = mutableListOf<PokemonResults>()

    inner class PokemonViewHolder(itemView : View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var pokemonResults: PokemonResults
        private val pokemonImg : ImageView = itemView.findViewById(R.id.pokemonImg)
        private val pokemonName : TextView = itemView.findViewById(R.id.pokemonName)
        override fun onClick(v: View) {
            itemViewClickListener.onClickLister(adapterPosition,v,pokemonResults)
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(pokemonResults: PokemonResults){
            this.pokemonResults = pokemonResults
            this.pokemonName.text = pokemonResults.name
            bindImage(this.pokemonImg, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                    "pokemon/other/official-artwork/${getId(pokemonList[adapterPosition].url)}.png")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.pokemon_list_view,parent,false))
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
//        bindImage(holder.pokemonImg, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
//        "pokemon/other/official-artwork/${getId(pokemonList[position].url)}.png")
//        holder.pokemonName.text = pokemonList[position].name

        val item = pokemonList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun getPokemonList(list : List<PokemonResults>){
        pokemonList.clear()
        pokemonList.addAll(list)
        notifyDataSetChanged()
    }
}