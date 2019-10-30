package com.coder.starwars.di.module

import com.coder.starwars.ui.StarWarsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module for injection for activities
 */
@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = arrayOf(MainActivityFragmentBuildersModule::class))
    internal abstract fun contributeCharacterActivity(): StarWarsActivity
}