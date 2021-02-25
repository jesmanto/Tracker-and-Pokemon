package com.jesse.pokemon.screens

import android.content.Intent
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.jesse.pokemon.adapter.PokemonListAdapter
import com.jesse.pokemon.data.PokemonResults
import com.jesse.pokemon.data.Root
import com.jesse.pokemon.databinding.ActivityMainBinding
import com.jesse.pokemon.network.PokemonApi
import com.jesse.pokemon.utils.ItemViewClickListener
import io.reactivex.disposables.Disposable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity(), ItemViewClickListener {

    lateinit var subscription: Subscription

    lateinit var binding: ActivityMainBinding

    lateinit var networkDisposable: Disposable

    private val pokemonAdapter = PokemonListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting an adapter to recyclerView
        binding.pokemonRecyclerView.adapter = pokemonAdapter

        binding.toolbar.searchBtn.setOnClickListener {
            clearFields()
            binding.searchBox.visibility = View.VISIBLE
        }

        // When the user requests to see a particular number of pokemon
        // characters, this button is pressed and [fetchPokens()] is triggered
        binding.searchButton.setOnClickListener {
            fetchPokemon()
            binding.searchBox.visibility = View.INVISIBLE
        }
    }

    /**
     * This method makes a network request to the API
     * to fetch pokemon characters
     */
    private fun getPokemons(offset: Int, limit: Int) {
        subscription = PokemonApi.retrofitService
            .getPropertiesAsync(offset, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Root> {

                /**
                 * Invoked when the network call is complete
                 */
                override fun onCompleted() {
                    Log.d("POKEMON", "In Completed()")
                    binding.progressBar.visibility = View.GONE
                    binding.pokemonRecyclerView.visibility = View.VISIBLE
                    binding.noNetwork.visibility = View.GONE
                    binding.noNetworkIcon.visibility = View.GONE
                }

                /**
                 * Notifies the Observer that the Observable has experienced an error condition.
                 * If the Observable calls this method, it will not thereafter call onNext or onCompleted.
                 */
                override fun onError(e: Throwable?) {
                    Log.d("POKEMON", "In Error()")
                    e?.printStackTrace()
                }

                /**
                 * Provides the Observer with a new item to observe.
                 */
                override fun onNext(t: Root?) {
                    Log.d("POKEMON", "In Next()")
                    Log.d("POKEMON", "${t?.results}")
                    pokemonAdapter.getPokemonList(t?.results!!)
                }

            })
    }

    /**
     * This method is invoked whenever a custom
     * search is made by a user.
     *
     * It checks for an empty field and checks for
     * NumberFormatException
     */
    private fun fetchPokemon() {
        val offset: Int = if (binding.offset.text.toString() == "") {
            0
        } else {
            Integer.parseInt(binding.offset.text.toString())
        }

        try {
            val limit = Integer.parseInt(binding.limit.text.toString())
            getPokemons(offset, limit)
        } catch (e: NumberFormatException) {
            Toast.makeText(
                this, "Please enter the quantity of pokemon characters" +
                        "you wish to see", Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * This method clears the editText fields
     */
    private fun clearFields() {
        binding.offset.text.clear()
        binding.limit.text.clear()
    }

    override fun onDestroy() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroy()
    }

    /**
     * This listens to a click event on any pokemon at
     * any position
     */
    override fun onClickLister(position: Int, view: View, pokemonResults: PokemonResults) {
        val intent = Intent(this, ProfileActivity::class.java).putExtra("pokemon", pokemonResults)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Checking for the networ connectivity to ensure the
         * screen is not left blank when the network is off
         * and when the application is loading
         */
        networkDisposable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe { connectivity: Connectivity ->
                val state = connectivity.state
                if (state == NetworkInfo.State.CONNECTED) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.noNetwork.visibility = View.GONE
                    binding.noNetworkIcon.visibility = View.GONE
                    getPokemons(2, 20)
                } else if (state == NetworkInfo.State.DISCONNECTED) {
                    binding.progressBar.visibility = View.GONE
                    binding.pokemonRecyclerView.visibility = View.GONE
                    binding.noNetwork.visibility = View.VISIBLE
                    binding.noNetworkIcon.visibility = View.VISIBLE
                }
            }
    }
}