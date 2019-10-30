package com.coder.starwars.ui.detail.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.coder.starwars.R
import com.coder.starwars.data.network.model.FilmDetailsModel
import com.coder.starwars.util.extensions.isValid
import kotlinx.android.synthetic.main.film_details_layout.view.*

/**
 * Layout to hold film details
 */
class FilmDetailsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.film_details_layout, this, true)
    }

    fun filmDetails(details: FilmDetailsModel) {
        if (details.title.isValid())
            tv_fim_name.text = details.title

        if (details.releaseDate.isValid())
            tv_fim_release_date.text =
                String.format(context.getString(R.string.released_on), details.releaseDate)

        if (details.openingCrawl.isValid())
            tv_film_crawl.text = details.openingCrawl
    }

}