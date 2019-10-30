package com.coder.starwars.data.network

import com.coder.starwars.data.network.response.FilmResponse
import com.coder.starwars.data.network.response.HomeWorldResponse
import com.coder.starwars.data.network.response.SpeciesResponse
import com.coder.starwars.data.network.model.CharacterDetailsModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Network data source for Character details screen
 */
@Singleton
class CharacterDetailsNetworkDataSource @Inject constructor(private val startWarsApi: StarWarsApi) {
    fun getCharacterDetails(url: String): Single<CharacterDetailsModel> =
        startWarsApi.getCharacterDetails(url)

    fun getCharacterSpecies(url: String?): Single<SpeciesResponse> =
        startWarsApi.getCharacterSpecies(url)

    fun getCharacterFilm(url: String?): Single<FilmResponse> = startWarsApi.getCharacterFilm(url)

    fun getCharacterHomeworld(url: String?): Single<HomeWorldResponse> =
        startWarsApi.getCharacterHomeworld(url)
}