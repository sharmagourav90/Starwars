package com.coder.starwars.ui.detail.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.coder.starwars.R
import com.coder.starwars.data.network.model.SpeciesDetailsModel
import com.coder.starwars.util.extensions.gone
import com.coder.starwars.util.extensions.isValid
import kotlinx.android.synthetic.main.species_details_layout.view.*

/**
 * Layout to hold species details
 */
class SpeciesDetailsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.species_details_layout, this, true)
    }

    fun specieDetails(detailsModel: SpeciesDetailsModel) {
        if (detailsModel.species.isValid())
            tv_species_name.text = detailsModel.species
        else tv_species_name.gone()

        if (detailsModel.language.isValid())
            tv_species_language.text =
                String.format(context.getString(R.string.speak), detailsModel.language)
        else tv_species_language.gone()

        if (detailsModel.homeworld.isValid())
            tv_home_world.text =
                String.format(context.getString(R.string.live_in), detailsModel.homeworld)
        else tv_home_world.gone()

        if (detailsModel.population.isValid())
            tv_population.text =
                String.format(context.getString(R.string.population), detailsModel.population)
        else tv_population.gone()
    }

}