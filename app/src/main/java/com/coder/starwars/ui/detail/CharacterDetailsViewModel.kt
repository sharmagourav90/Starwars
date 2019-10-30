package com.coder.starwars.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coder.starwars.data.network.model.FilmDetailsModel
import com.coder.starwars.data.network.model.SpeciesDetailsModel
import com.coder.starwars.data.network.response.HomeWorldResponse
import com.coder.starwars.data.network.model.CharacterDetailsModel
import com.coder.starwars.data.repository.CharacterDetailsRepository
import com.coder.starwars.ui.base.BaseViewModel
import com.coder.starwars.util.UnitUtils.cmToFeet
import com.coder.starwars.util.extensions.hide
import com.coder.starwars.util.extensions.show
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * View model for character details screen
 */
class CharacterDetailsViewModel @Inject constructor(private val repository: CharacterDetailsRepository) :
    BaseViewModel() {

    private val characterDetails = MutableLiveData<CharacterDetailsModel>()

    private var speciesName: String? = null
    private var specieLanguage: String? = null

    /**
     * Get character details
     */
    fun getCharacterDetails(url: String): LiveData<CharacterDetailsModel> {
        if (characterDetails.value == null) {

            _loading.show()

            repository.getCharacterDetails(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map {
                    val details = it.copy(heightFtInches = cmToFeet(it.heightCentimeters))
                    characterDetails.postValue(details)
                    return@map details
                }
                .flatMap { details ->
                    Single.zip(speciesHomeWorld(details), films(details),
                        BiFunction { speciesDetails: List<SpeciesDetailsModel>, filmDetails: List<FilmDetailsModel> ->
                            details.copy(speciesDetails = speciesDetails, filmDetails = filmDetails)
                        })
                }
                .subscribe({
                    _loading.hide()
                    characterDetails.postValue(it)
                }, { handleError(it) })
                .addTo(disposable)
        }
        return characterDetails
    }

    /**
     * Get Species and HomeWorld
     */
    private fun speciesHomeWorld(details: CharacterDetailsModel): Single<List<SpeciesDetailsModel>> {
        return Flowable.fromIterable(details.speciesUrl)
            .flatMapSingle { speciesUrl -> repository.getCharacterSpecies(speciesUrl) }
            .flatMapSingle { species ->
                speciesName = species.name
                specieLanguage = species.language
                return@flatMapSingle repository
                    .getCharacterHomeworld(species.homeworldUrl)
                    .onErrorReturn { HomeWorldResponse() }
            }
            .observeOn(Schedulers.computation())
            .map { homeworldResponse ->
                SpeciesDetailsModel(
                    speciesName, specieLanguage,
                    homeworldResponse.name, homeworldResponse.population
                )
            }
            .toList()
    }

    /**
     * Get films details
     */
    private fun films(details: CharacterDetailsModel): Single<List<FilmDetailsModel>> {
        return Flowable.fromIterable(details.filmUrls)
            .flatMapSingle { filmUrl -> repository.getCharacterFilm(filmUrl) }
            .map { film -> FilmDetailsModel(film.title, film.releaseDate, film.openingCrawl) }
            .toList()
    }
}
