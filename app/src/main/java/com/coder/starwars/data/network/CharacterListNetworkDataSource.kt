package com.coder.starwars.data.network

import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.data.network.response.CharactersListResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Network data source for Character list screen
 */
@Singleton
class CharacterListNetworkDataSource @Inject constructor(private val starWarsApi: StarWarsApi) {
    fun getCharacters(url: String)
            : Single<CharactersListResponse<List<CharacterItemModel>>> =
        starWarsApi.getCharacters(url)

    fun searchCharacter(query: String)
            : Single<CharactersListResponse<List<CharacterItemModel>>> =
        starWarsApi.searchCharacter(query)
}