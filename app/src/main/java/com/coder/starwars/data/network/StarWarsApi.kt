package com.coder.starwars.data.network

import com.coder.starwars.data.network.response.FilmResponse
import com.coder.starwars.data.network.response.HomeWorldResponse
import com.coder.starwars.data.network.response.SpeciesResponse
import com.coder.starwars.data.network.model.CharacterDetailsModel
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.data.network.response.CharactersListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * StarWars Api
 */
interface StarWarsApi {
    @GET
    fun getCharacters(@Url url: String): Single<CharactersListResponse<List<CharacterItemModel>>>

    @GET("people")
    fun searchCharacter(@Query("search") query: String): Single<CharactersListResponse<List<CharacterItemModel>>>

    @GET
    fun getCharacterDetails(@Url url: String): Single<CharacterDetailsModel>

    @GET
    fun getCharacterSpecies(@Url url: String?): Single<SpeciesResponse>

    @GET
    fun getCharacterHomeworld(@Url url: String?): Single<HomeWorldResponse>

    @GET
    fun getCharacterFilm(@Url url: String?): Single<FilmResponse>
}