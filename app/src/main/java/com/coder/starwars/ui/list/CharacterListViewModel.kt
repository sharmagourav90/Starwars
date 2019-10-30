package com.coder.starwars.ui.list

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.data.network.response.CharactersListResponse
import com.coder.starwars.data.repository.CharacterListRepository
import com.coder.starwars.ui.base.BaseViewModel
import com.coder.starwars.util.extensions.hide
import com.coder.starwars.util.extensions.show
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * ViewModel for CharacterListFragment
 */
class CharacterListViewModel @Inject
constructor(val repository: CharacterListRepository) : BaseViewModel() {
    // CharacterList mutablelivedata, exposed livedata
    private val _characters = MutableLiveData<List<CharacterItemModel>>()
    val characters: LiveData<List<CharacterItemModel>> by lazy { _characters }

    private var nextPageUrl: String? = ""
    private var processing: Boolean = false

    // paginationLoading mutablelivedata, exposed livedata
    private val _paginationLoading = MutableLiveData<Boolean>()
    val paginationLoading: LiveData<Boolean> by lazy { _paginationLoading }

    private val initial = "people"

    // Initial loading of character list
    fun initialLoad() {
        if (_characters.value != null && !_characters.value.isNullOrEmpty()) return

        _loading.show()
        getCharacters(url = initial, resetItems = true)
    }

    /**
     * Get the character list from server
     */
    private fun getCharacters(url: String, resetItems: Boolean) {
        if (processing) return
        processing = true
        handleCharacters(getCharacters(url), resetItems)
    }

    private fun getCharacters(url: String) = repository.getCharacters(url)

    /**
     * Handle the success response from server
     */
    private fun handleCharacters(
        result: Single<CharactersListResponse<List<CharacterItemModel>>>,
        resetItems: Boolean
    ) {
        result
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                nextPageUrl = response.next
                return@map response.results
            }
            .map { searchModels ->
                appendOrSetResults(resetItems, _characters.value, searchModels)
            }
            .subscribe({
                _loading.hide()
                _paginationLoading.hide()
                _characters.postValue(it)
                processing = false
            }, {
                _paginationLoading.hide()
                handleError(it)
                processing = false
            })
            .addTo(disposable)
    }

    /**
     * Append or set results to character list livedata
     */
    private fun appendOrSetResults(
        resetItems: Boolean,
        existingData: List<CharacterItemModel>?,
        newData: List<CharacterItemModel>
    ): List<CharacterItemModel> {
        val finalData = mutableListOf<CharacterItemModel>()
        if (resetItems || existingData.isNullOrEmpty())
            finalData.addAll(newData)
        else {
            finalData.addAll(existingData)
            finalData.addAll(newData)
        }
        return finalData
    }

    /**
     * Load next page
     */
    fun loadNextPage() {
        nextPageUrl?.run {
            _paginationLoading.show()
            getCharacters(this, false)
        }
    }

    /**
     * Search character with query
     */
    fun searchCharacter(query: String?) {
        if (query.isNullOrEmpty()) return

        _loading.show()
        handleCharacters(searchedCharacter(query), true)

    }

    private fun searchedCharacter(query: String) = repository.searchCharacter(query)

    /**
     * Refresh characters list
     */
    fun refreshCharacters() {
        _loading.show()
        getCharacters(url = initial, resetItems = true)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setCharactersList(characters: List<CharacterItemModel>?) {
        _characters.postValue(characters)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun getInitialApi() = initial

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setNextPageUrl(url: String?) {
        nextPageUrl = url
    }
}