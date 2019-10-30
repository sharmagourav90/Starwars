package com.coder.starwars.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Film reponse from character details
 */
data class FilmResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("opening_crawl") val openingCrawl: String?
)