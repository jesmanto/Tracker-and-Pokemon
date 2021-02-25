package com.jesse.pokemon.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

//import kotlinx.parcelize.Parcelize


data class Ability (
    var ability: Ability2,
    var is_hidden: Boolean,
    var slot:Int = 0
)


data class Ability2 (var name: String,
                     var url: String)

data class DreamWorld (
    var front_default: String,
    var front_female: Any
)

data class HeldItem (var item: Item)

data class Icons (var front_default: String,
                  var front_female: Any)

data class Item (
    var name: String
)

data class Moves(var move: Move)

data class Move (var name: String)

data class OfficialArtwork(var front_default: String)

data class Other (
    @Json(name = "official-artwork")
    var officialArtwork: OfficialArtwork
)

data class Pokemon (
    val abilities: List<Ability>,
    val base_experience: Int,
    val height: Int,
    val held_items: List<HeldItem>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Moves>,
    val name: String,
    val order: Int,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val weight: Int
)

@Parcelize
data class PokemonResults(
    val name : String,
    val url : String
) : Parcelable

data class Root (
    val next : String?,
    val previous : String?,
    val results : List<PokemonResults>
)

data class Species (var name: String)

data class Sprites (
    var back_default: String?,
    var back_female: String?,
    var back_shiny: String?,
    var back_shiny_female: String?,
    var front_default: String?,
    var front_female: String?,
    var front_shiny: String?,
    var front_shiny_female: String?,
    var other : Other
)

data class Stat (
    var stat: Stat2
)

data class Stat2(var name : String)

fun getId(url: String) : String{
    val newUrl = url.split("/")
    return newUrl[newUrl.size-2]
}