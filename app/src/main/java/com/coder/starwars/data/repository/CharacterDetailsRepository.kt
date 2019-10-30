package com.coder.starwars.data.repository

import com.coder.starwars.data.network.CharacterDetailsNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Character details repository class
 */
@Singleton
class CharacterDetailsRepository @Inject constructor(private val dataSource: CharacterDetailsNetworkDataSource) {

    fun getCharacterDetails(url: String) = dataSource.getCharacterDetails(url)

    fun getCharacterSpecies(url: String?) = dataSource.getCharacterSpecies(url)

    fun getCharacterFilm(url: String?) = dataSource.getCharacterFilm(url)

    fun getCharacterHomeworld(url: String?) = dataSource.getCharacterHomeworld(url)

}