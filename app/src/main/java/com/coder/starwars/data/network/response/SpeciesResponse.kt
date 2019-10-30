package com.coder.starwars.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Species response
 */
data class SpeciesResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("homeworld") val homeworldUrl: String?
)
