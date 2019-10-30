package com.coder.starwars.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Response object for page loading ,
 * count - total number of getCharacters
 * next - next page URL
 * previous - previous page URL
 * results - List of Characters Data
 */
data class CharactersListResponse<T>(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: T?
)