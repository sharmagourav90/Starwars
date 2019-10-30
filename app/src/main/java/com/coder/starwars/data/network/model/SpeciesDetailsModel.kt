package com.coder.starwars.data.network.model

/**
 * Model for species details
 */
data class SpeciesDetailsModel(
    val species: String?,
    val language: String?,
    val homeworld: String?,
    val population: String?
)