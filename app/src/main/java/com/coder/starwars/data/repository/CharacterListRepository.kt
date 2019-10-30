package com.coder.starwars.data.repository

import com.coder.starwars.data.network.CharacterListNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Character list repository class
 */
@Singleton
class CharacterListRepository @Inject constructor(private val dataSource: CharacterListNetworkDataSource) {

    fun getCharacters(url: String) = dataSource.getCharacters(url)

    fun searchCharacter(query: String) = dataSource.searchCharacter(query)
}