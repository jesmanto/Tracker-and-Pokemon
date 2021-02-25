package com.jesse.pokemon.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.jesse.pokemon.R
import com.jesse.pokemon.adapter.MoveAdapter
import com.jesse.pokemon.data.Pokemon
import com.jesse.pokemon.data.PokemonResults
import com.jesse.pokemon.databinding.ActivityProfileBinding
import com.jesse.pokemon.network.PokemonApi
import com.jesse.pokemon.utils.bindImage
import com.jesse.pokemon.utils.getId
import retrofit2.http.Url
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    lateinit var subscription: Subscription
    lateinit var bundle: PokemonResults

    lateinit var moveAdapter: MoveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bundle = intent.getParcelableExtra("pokemon")!!
        Log.d("ID", getId(bundle.url))

        moveAdapter = MoveAdapter()
        binding.moveRecyclerView.adapter = moveAdapter

        bindImage(
            binding.pokemonImage,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                    "pokemon/other/official-artwork/${getId(bundle.url)}.png"
        )
        getPokemonDetails(getId(bundle.url))
    }

    /**
     * This method makes a network request to the API
     * to fetch pokemon characters personal details
     */
    private fun getPokemonDetails(id: String){
        subscription = PokemonApi.retrofitService
            .getPokemon(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Pokemon> {
                override fun onCompleted() {
                    Log.d("DETAILS","In Completed()")
                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

                override fun onNext(t: Pokemon?) {
                    Log.d("DETAILS","In Next()")
                    Log.d("POKEMON","${t?.moves}")
                    binding.height.text = getString(R.string.height, t?.height)
                    binding.weight.text = getString(R.string.weight, t?.weight)
                    getImage(binding.backDefault,t?.sprites?.back_default)
                    getImage(binding.backFemale,t?.sprites?.back_female)
                    getImage(binding.backShiny,t?.sprites?.back_shiny)
                    getImage(binding.backShinyFemale,t?.sprites?.back_shiny_female)
                    getImage(binding.frontDefault,t?.sprites?.front_default)
                    getImage(binding.frontFemale,t?.sprites?.front_female)
                    getImage(binding.frontShiny,t?.sprites?.front_shiny)
                    getImage(binding.frontShinyFemale,t?.sprites?.front_shiny_female)

                    moveAdapter.populateMoveList(t!!.moves)

                }
            })
    }

    /**
     * gets the url of the image and apply glide function to it
     * to convert the url into a drawable
     */
    private fun getImage(imgView: ImageView, url: String?){
        if (url != null){
            imgView.visibility = View.VISIBLE
            bindImage(imgView,url)
        }

    }
}