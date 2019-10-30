package com.coder.starwars.ui

import android.os.Bundle
import com.coder.starwars.R
import com.coder.starwars.ui.detail.CharacterDetailsFragment
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.ui.list.CharacterListFragment
import dagger.android.support.DaggerAppCompatActivity

/**
 * StarWarsActivity contains both the fragment List and Detail and
 * swaps as per the request
 */
class StarWarsActivity : DaggerAppCompatActivity(), CharacterListFragment.CharacterNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starwars)

        showListFragment()
    }

    private fun showListFragment() {
        val tag = CharacterListFragment.TAG
        val fragment = CharacterListFragment.newInstance()

        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commitAllowingStateLoss()
    }

    override fun showCharacterDetails(characterItem: CharacterItemModel) {
        val tag = CharacterDetailsFragment.TAG
        val fragment = CharacterDetailsFragment.newInstance(characterItem)

        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}
