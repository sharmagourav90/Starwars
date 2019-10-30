package com.coder.starwars.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.coder.starwars.data.repository.CharacterDetailsRepository
import com.coder.starwars.data.network.CharacterDetailsNetworkDataSource
import com.coder.starwars.data.network.StarWarsApi
import com.coder.starwars.data.network.model.CharacterDetailsModel
import com.coder.starwars.testhelper.SampleDataForTest.characterUrl
import com.coder.starwars.testhelper.SampleDataForTest.charcterDetails
import com.coder.starwars.testhelper.SampleDataForTest.secondFilmUrl
import com.coder.starwars.testhelper.SampleDataForTest.homeworldUrl
import com.coder.starwars.testhelper.SampleDataForTest.parseCharacterDetails
import com.coder.starwars.testhelper.SampleDataForTest.parsesecondFilm
import com.coder.starwars.testhelper.SampleDataForTest.parseSixthFilm
import com.coder.starwars.testhelper.SampleDataForTest.parseHomeWorld
import com.coder.starwars.testhelper.SampleDataForTest.parseCharacterSpecies
import com.coder.starwars.testhelper.SampleDataForTest.sixthFilmUrl
import com.coder.starwars.testhelper.SampleDataForTest.speciesUrl
import com.coder.starwars.testhelper.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Class containing test cases for character details fetching.
 * Character Species, Character HomeWorld, Character Films
 */
@RunWith(JUnit4::class)
class CharacterDetailsViewModelTest {
    // Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    // Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    // DataSource, Repo, ViewModel
    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var repository: CharacterDetailsRepository
    private lateinit var dataSource: CharacterDetailsNetworkDataSource

    // Api Mocking
    private val starWarsApi = mock<StarWarsApi>()

    // Loading and Error observers mocking
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Throwable>>()

    //Create a test observer for details
    private val detailsObserver = mock<Observer<CharacterDetailsModel>>()

    @Before
    fun setUp() {
        // DataSource, Repo, ViewModel instantiation
        dataSource = CharacterDetailsNetworkDataSource(starWarsApi)
        repository = CharacterDetailsRepository(dataSource)
        viewModel = CharacterDetailsViewModel(repository)

        // Attach observers to loading and error
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
    }

    /**
     * Verify character details fetched.
     */
    @Test
    fun testCharacterDetailsSuccess() {
        // Mock success data response from starWarsApi
        whenever(starWarsApi.getCharacterDetails(characterUrl)).doReturn(
            Single.just(
                parseCharacterDetails()
            )
        )
        whenever(starWarsApi.getCharacterSpecies(speciesUrl)).doReturn(
            Single.just(
                parseCharacterSpecies()
            )
        )
        whenever(starWarsApi.getCharacterHomeworld(homeworldUrl)).doReturn(
            Single.just(
                parseHomeWorld()
            )
        )
        whenever(starWarsApi.getCharacterFilm(secondFilmUrl)).doReturn(
            Single.just(
                parsesecondFilm()
            )
        )
        whenever(starWarsApi.getCharacterFilm(sixthFilmUrl)).doReturn(
            Single.just(
                parseSixthFilm()
            )
        )

        // Trigger the load and attach the observer
        viewModel.getCharacterDetails(characterUrl).observeForever(detailsObserver)

        // Verify loading state changes
        verify(loadingObserver).onChanged(true)

        // Verify correct methods are called in the starWarsApi
        verify(starWarsApi).getCharacterDetails(characterUrl)
        verify(starWarsApi).getCharacterSpecies(speciesUrl)
        verify(starWarsApi).getCharacterHomeworld(homeworldUrl)
        verify(starWarsApi).getCharacterFilm(secondFilmUrl)
        verify(starWarsApi).getCharacterFilm(sixthFilmUrl)

        // Verify loading state changes again
        verify(loadingObserver).onChanged(false)

        // Verify data pushed to UI is the same we expected
        verify(detailsObserver).onChanged(charcterDetails)

        // Verify error is never pushed to the UI
        verify(errorObserver, never()).onChanged(any())

        println("Character details fetched success.")
    }

}