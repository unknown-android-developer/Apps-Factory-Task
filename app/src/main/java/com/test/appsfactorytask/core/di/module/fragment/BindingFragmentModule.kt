package com.test.appsfactorytask.core.di.module.fragment

import com.test.appsfactorytask.core.di.scope.FragmentScope
import com.test.appsfactorytask.screen.albums.di.AlbumsFragmentModule
import com.test.appsfactorytask.screen.albums.view.AlbumsFragment
import com.test.appsfactorytask.screen.searchartist.di.SearchArtistFragmentModule
import com.test.appsfactorytask.screen.searchartist.view.SearchArtistFragment
import com.test.appsfactorytask.screen.topalbums.di.TopAlbumsFragmentModule
import com.test.appsfactorytask.screen.topalbums.view.TopAlbumsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BindingFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AlbumsFragmentModule::class])
    fun albumsFragmentInjector(): AlbumsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [SearchArtistFragmentModule::class])
    fun searchArtistFragmentInjector(): SearchArtistFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TopAlbumsFragmentModule::class])
    fun topAlbumsFragmentInjector(): TopAlbumsFragment
}