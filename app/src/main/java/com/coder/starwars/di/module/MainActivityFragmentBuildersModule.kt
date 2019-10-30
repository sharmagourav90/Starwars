package com.coder.starwars.di.module

import com.coder.starwars.ui.detail.CharacterDetailsFragment
import com.coder.starwars.ui.list.CharacterListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeCharacterListFragment(): CharacterListFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCharacterDetailsFragment(): CharacterDetailsFragment
}