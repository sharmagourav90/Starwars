package com.coder.starwars.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Homeworld Response
 */
data class HomeWorldResponse(
    @SerializedName("name") val name: String? = "",
    @SerializedName("population") val population: String? = ""
)
