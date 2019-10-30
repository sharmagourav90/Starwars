package com.coder.starwars.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Character details data model
 */
data class CharacterDetailsModel(
    @SerializedName("url") val url: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("birth_year") val birthYear: String?,
    @SerializedName("height") val heightCentimeters: String?,
    val heightFtInches: Pair<String, String>?,
    @SerializedName("species") val speciesUrl: List<String>?,
    val speciesDetails: List<SpeciesDetailsModel>?,
    @SerializedName("films") val filmUrls: List<String>?,
    val filmDetails: List<FilmDetailsModel>?
)