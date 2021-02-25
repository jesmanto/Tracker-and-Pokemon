package com.jesse.pokemon.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jesse.pokemon.R

/**
 * This function uses glide to convert image URL to a drawable
 */
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.broken_image)
            )
            .into(imgView)
    }
}

/**
 * This function takes the url of a pokemon character and extracts the id
 */
fun getId(url: String) : String{
    val newUrl = url.split("/")
    return newUrl[newUrl.size-2]
}