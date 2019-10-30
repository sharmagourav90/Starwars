package com.coder.starwars.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.coder.starwars.data.network.CharacterListNetworkDataSource
import com.coder.starwars.data.network.StarWarsApi
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.data.repository.CharacterListRepository
import com.coder.starwars.testhelper.SampleDataForTest.initialSuccessData
import com.coder.starwars.testhelper.SampleDataForTest.parseCharacterSearch
import com.coder.starwars.testhelper.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Class contains test case for the CharacterList screen.
 * Loading initial data, next page.
 * Search character.
 * Refresh character list.
 */
@RunWith(JUnit4::class)
class CharacterListViewModelTest {
    // Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    // Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    // DataSource, Repo, ViewModel
    private lateinit var viewModel: CharacterListViewModel
    private lateinit var repository: CharacterListRepository
    private lateinit var dataSource: CharacterListNetworkDataSource

    // Api Mocking
    private val starWarsApi = mock<StarWarsApi>()

    // Loading and Error observers mocking
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Throwable>>()

    // Pagination and Character list observers mocking
    private val paginationObserver = mock<Observer<Boolean>>()
    private val charactersListObserver = mock<Observer<List<CharacterItemModel>>>()

    @Before
    fun init() {
        // DataSource, Repo, ViewModel instantiation
        dataSource = CharacterListNetworkDataSource(starWarsApi)
        repository = CharacterListRepository(dataSource)
        viewModel = CharacterListViewModel(repository)

        // Attach observers to loading and error
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)

        // Attach observers to pagination and getCharacters list
        viewModel.paginationLoading.observeForever(paginationObserver)
        viewModel.characters.observeForever(charactersListObserver)
    }

    /**
     * Verify initial load with viewModel.getInitialApi()
     */
    @Test
    fun testInitialLoad() {
        // Mock success data response from starWarsApi
        whenever(starWarsApi.getCharacters(viewModel.getInitialApi())).doReturn(
            Single.just(
                initialSuccessData
            )
        )

        // Trigger the load
        viewModel.initialLoad()

        // Verify loading state changes
        verify(loadingObserver).onChanged(true)

        // Verify correct methods are called in the starWarsApi
        verify(starWarsApi).getCharacters(viewModel.getInitialApi())

        // Verify loading state changes again
        verify(loadingObserver).onChanged(false)

        // Verify data pushed to UI
        verify(charactersListObserver).onChanged(any())

        // Verify error is never pushed to the UI
        verify(errorObserver, never()).onChanged(any())

        // Verify the getCharacters are reset and not appended
        assertEquals(10, viewModel.characters.value?.size)

        println("Initial load success.")
    }

    /**
     * Verify next page load with initialSuccessData.next
     */
    @Test
    fun testLoadNextPage() {
        // Mock success data response from starWarsApi
        whenever(starWarsApi.getCharacters(initialSuccessData.next!!)).doReturn(
            Single.just(
                initialSuccessData
            )
        )

        // Setup viewModel and nextUrl
        viewModel.setCharactersList(initialSuccessData.results)
        val nextUrl = initialSuccessData.next

        // Set valid next page url
        viewModel.setNextPageUrl(nextUrl)

        // Trigger next load
        viewModel.loadNextPage()

        // Verify pagination state changes
        verify(paginationObserver).onChanged(true)

        // Verify correct methods are called in the starWarsApi
        verify(starWarsApi).getCharacters(initialSuccessData.next!!)

        // Verify pagination state changes again
        verify(paginationObserver).onChanged(false)

        // Verify data pushed to UI
        verify(charactersListObserver, atLeastOnce()).onChanged(any())

        // Verify error is never pushed to the UI
        verify(errorObserver, never()).onChanged(any())

        // Verify the getCharacters are not reset and appended instead
        assertEquals(20, viewModel.characters.value?.size)

        println("Load next page success.")
    }

    /**
     * Verify search character success
     */
    @Test
    fun testSearchCharacter() {
        val query = "luke"
        // Mock success data response from starWarsApi
        whenever(starWarsApi.searchCharacter(query)).doReturn(Single.just(parseCharacterSearch()))

        // Setup ViewModel with initial data
        viewModel.setCharactersList(initialSuccessData.results)

        // Trigger search
        viewModel.searchCharacter(query)

        // Verify loading state changes
        verify(loadingObserver).onChanged(true)

        // Verify correct methods are called in the starWarsApi
        verify(starWarsApi).searchCharacter(query)

        // Verify loading state changes again
        verify(loadingObserver).onChanged(false)

        // Verify data pushed to UI
        verify(charactersListObserver, atLeastOnce()).onChanged(any())

        // Verify error is never pushed to the UI
        verify(errorObserver, never()).onChanged(any())

        // Verify the getCharacters are reset and not appended
        assertEquals(1, viewModel.characters.value?.size)

        println("Search character success.")
    }

    /**
     * Verify refreshing of list
     */
    @Test
    fun testRefresh() {
        // Mock success data response from starWarsApi
        whenever(starWarsApi.getCharacters(viewModel.getInitialApi())).doReturn(
            Single.just(
                initialSuccessData
            )
        )

        // Trigger the load
        viewModel.refreshCharacters()

        // Verify loading state changes
        verify(loadingObserver).onChanged(true)

        // Verify correct methods are called in the starWarsApi
        verify(starWarsApi).getCharacters(viewModel.getInitialApi())

        // Verify loading state changes again
        verify(loadingObserver).onChanged(false)

        // Verify data pushed to UI
        verify(charactersListObserver).onChanged(any())

        // Verify error is never pushed to the UI
        verify(errorObserver, never()).onChanged(any())

        // Verify the getCharacters are reset and not appended
        assertEquals(10, viewModel.characters.value?.size)

        println("List refresh sucess.")
    }
}