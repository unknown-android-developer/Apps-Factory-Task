package com.test.appsfactorytask.screen.searchartist.di

import androidx.lifecycle.ViewModel
import com.test.appsfactorytask.core.di.module.viewmodel.ViewModelKey
import com.test.appsfactorytask.core.di.scope.FragmentScope
import com.test.appsfactorytask.screen.searchartist.domain.SearchArtistInteractor
import com.test.appsfactorytask.screen.searchartist.domain.SearchArtistInteractorImpl
import com.test.appsfactorytask.screen.searchartist.viewmodel.SearchArtistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchArtistFragmentModule {

    @Binds
    @FragmentScope
    fun bindSearchArtistInteractor(searchArtistInteractor: SearchArtistInteractorImpl): SearchArtistInteractor

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(SearchArtistViewModel::class)
    fun bindViewModel(viewModel: SearchArtistViewModel): ViewModel
}