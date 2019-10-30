package com.coder.starwars.testhelper

import com.coder.starwars.data.network.response.HomeWorldResponse
import com.coder.starwars.data.network.response.SpeciesResponse
import com.coder.starwars.data.network.model.*
import com.coder.starwars.data.network.response.FilmResponse
import com.coder.starwars.data.network.response.CharactersListResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Class containing sample data for character list and character details screen.
 */
object SampleDataForTest {
    // URLs for testing
    private const val baseUrl = "https://swapi.co/api/"
    const val characterUrl = "${baseUrl}people/1/"
    const val speciesUrl = "${baseUrl}species/1/"
    const val homeworldUrl = "${baseUrl}planets/9/"
    const val secondFilmUrl = "${baseUrl}films/2/"
    const val sixthFilmUrl = "${baseUrl}films/6/"

    // Gson instance for parsing
    private val gson = Gson()

    // Initial data for loading list
    val initialSuccessData = parseInitialData()

    // Search result data
    val charcterDetails = CharacterDetailsModel(
        url = characterUrl,
        name = "Luke Skywalker",
        birthYear = "19BBY",
        heightCentimeters = "172",
        heightFtInches = Pair("5.65", "67.72"),
        speciesUrl = listOf(speciesUrl),
        speciesDetails = listOf(speciesDetails()),
        filmUrls = listOf(secondFilmUrl, sixthFilmUrl),
        filmDetails = listOf(secondFilmDetails(), sixthFilmDetails())
    )

    // Species Details
    private fun speciesDetails() =
        SpeciesDetailsModel("Human", "Galactic Basic", "Coruscant", "1000000000000")

    // Second Film Details
    private fun secondFilmDetails() = FilmDetailsModel(
        "The Empire Strikes Back", "1980-05-17",
        "It is a dark time for the\r\nRebellion. Although the Death\r\nStar has been destroyed,\r\nImperial troops have driven the\r\nRebel forces from their hidden\r\nbase and pursued them across\r\nthe galaxy.\r\n\r\nEvading the dreaded Imperial\r\nStarfleet, a group of freedom\r\nfighters led by Luke Skywalker\r\nhas established a new secret\r\nbase on the remote ice world\r\nof Hoth.\r\n\r\nThe evil lord Darth Vader,\r\nobsessed with finding young\r\nSkywalker, has dispatched\r\nthousands of remote probes into\r\nthe far reaches of space...."
    )

    // Sixth Film Details
    private fun sixthFilmDetails() = FilmDetailsModel(
        "Revenge of the Sith", "2005-05-19",
        "War! The Republic is crumbling\r\nunder attacks by the ruthless\r\nSith Lord, Count Dooku.\r\nThere are heroes on both sides.\r\nEvil is everywhere.\r\n\r\nIn a stunning move, the\r\nfiendish droid leader, General\r\nGrievous, has swept into the\r\nRepublic capital and kidnapped\r\nChancellor Palpatine, leader of\r\nthe Galactic Senate.\r\n\r\nAs the Separatist Droid Army\r\nattempts to flee the besieged\r\ncapital with their valuable\r\nhostage, two Jedi Knights lead a\r\ndesperate mission to rescue the\r\ncaptive Chancellor...."
    )

    // Parse Home World
    fun parseHomeWorld(): HomeWorldResponse {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/character_homeworld"),
            HomeWorldResponse::class.java
        )
    }

    // Parse Second Film Response
    fun parsesecondFilm(): FilmResponse {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/character_film_2"),
            FilmResponse::class.java
        )
    }

    // Parse Sixth Film Response
    fun parseSixthFilm(): FilmResponse {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/character_film_6"),
            FilmResponse::class.java
        )
    }

    // Parse Character Details
    fun parseCharacterDetails(): CharacterDetailsModel {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/character_details"),
            CharacterDetailsModel::class.java
        )
    }

    // Parse Character Species
    fun parseCharacterSpecies(): SpeciesResponse {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/details/character_species"),
            SpeciesResponse::class.java
        )
    }

    // Parse Initial Data
    fun parseInitialData(): CharactersListResponse<List<CharacterItemModel>> {
        val responseModelToken: Type = object : TypeToken<CharactersListResponse<List<CharacterItemModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/initial_load"),
            responseModelToken
        )
    }

    // Parse Character Search
    fun parseCharacterSearch(): CharactersListResponse<List<CharacterItemModel>> {
        val responseModelToken: Type = object : TypeToken<CharactersListResponse<List<CharacterItemModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/luke_search"),
            responseModelToken
        )
    }
}