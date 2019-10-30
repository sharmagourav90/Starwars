package com.coder.starwars.ui.detail


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.coder.starwars.R
import com.coder.starwars.data.network.model.CharacterDetailsModel
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.di.ViewModelFactory
import com.coder.starwars.ui.detail.layouts.FilmDetailsLayout
import com.coder.starwars.ui.detail.layouts.SpeciesDetailsLayout
import com.coder.starwars.util.extensions.visible
import com.coder.starwars.ui.base.BaseFragment
import com.coder.starwars.util.extensions.isValid
import kotlinx.android.synthetic.main.actionbar_toolbar.*
import kotlinx.android.synthetic.main.fragment_character_details.*
import javax.inject.Inject

/**
 * Fragment for showing details of the character
 * fil, species, howeworld
 */
class CharacterDetailsFragment : BaseFragment() {

    override val layout = R.layout.fragment_character_details

    override val viewModelClass = CharacterDetailsViewModel::class.java

    @Inject
    lateinit var characterDetailsVMF: ViewModelFactory

    private val viewModel: CharacterDetailsViewModel by lazy { baseViewModel as CharacterDetailsViewModel }

    private var selectedCharacter: CharacterItemModel? = null

    companion object {
        const val TAG = "CharacterDetailsFragment"
        const val CHARACTER = "character"
        fun newInstance(character: CharacterItemModel) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHARACTER, character)
                }
            }
    }

    override fun provideViewModelFactory() = characterDetailsVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(CHARACTER))
                selectedCharacter = it.getParcelable(CHARACTER)
        }

        if (selectedCharacter == null) {
            showToast(R.string.data_issue)
            popBack()
            return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar, selectedCharacter?.name)

        // Disable swiperefreshlayout swipe functionality
        srl_details.isEnabled = false

        // Set the details passed from previous fragment
        tv_name.text = selectedCharacter?.name
        tv_yob.text = selectedCharacter?.birthYear

        selectedCharacter?.url?.run {
            // Trigger character details load
            viewModel.getCharacterDetails(this)
                .observe(this@CharacterDetailsFragment, Observer { details ->
                    showCharacterDetails(details)
                })
        }

    }

    // Set the character details to the UI
    private fun showCharacterDetails(details: CharacterDetailsModel) {

        tv_name.text = details.name
        tv_yob.text = details.birthYear

        if (details.heightCentimeters.isValid()) {
            tv_height_label.visible()
            tv_height.visible()
            tv_height.text =
                String.format(getString(R.string.centimeters), details.heightCentimeters)
        }

        if (details.heightFtInches != null) {
            tv_height_feet.visible()
            tv_height_feet.text = String.format(
                getString(R.string.feet_inches),
                details.heightFtInches.first, details.heightFtInches.second
            )
        }

        details.speciesDetails?.run {
            tv_species_label.visible()
            ll_species_details.visible()
            ll_species_details.removeAllViews()
            forEach {
                val specieDetailsView =
                    SpeciesDetailsLayout(parentActivity)
                specieDetailsView.specieDetails(it)
                ll_species_details.addView(specieDetailsView)
            }
        }

        details.filmDetails?.run {
            tv_films_label.visible()
            ll_films.visible()
            forEach {
                val filmDetailsView =
                    FilmDetailsLayout(parentActivity)
                filmDetailsView.filmDetails(it)
                ll_films.addView(filmDetailsView)
            }
        }
    }

    override fun hideLoading() {
        srl_details.isRefreshing = false
    }

    override fun showLoading() {
        srl_details.isRefreshing = true
    }
}
