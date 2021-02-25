package com.jesse.pokemon.network

//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jesse.pokemon.data.Pokemon
import com.jesse.pokemon.data.Root
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable


private const val BASE_URL = "https://pokeapi.co/api/v2/"

// Creating Gson instance
private val gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

//Creating a retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface PokemonApiService {
    @GET("pokemon")
    fun getPropertiesAsync(@Query("offset") end:Int, @Query("limit") start:Int):
            Observable<Root>

    @GET("pokemon/{id}/")
    fun getPokemon(@Path("id") id: String): Observable<Pokemon>
}

/**
 * Interface object to be called when making network call
 */
object PokemonApi {
    val retrofitService : PokemonApiService by lazy {
        retrofit.create(PokemonApiService::class.java)
    }
}