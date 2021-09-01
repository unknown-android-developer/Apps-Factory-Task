package com.test.appsfactorytask.screen.topalbums.di

import androidx.lifecycle.ViewModel
import com.test.appsfactorytask.core.di.module.viewmodel.ViewModelKey
import com.test.appsfactorytask.core.di.scope.FragmentScope
import com.test.appsfactorytask.screen.topalbums.domain.TopAlbumsInteractor
import com.test.appsfactorytask.screen.topalbums.domain.TopAlbumsInteractorImpl
import com.test.appsfactorytask.screen.topalbums.viewmodel.TopAlbumsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TopAlbumsFragmentModule {

    @Binds
    @FragmentScope
    fun bindTopAlbumsInteractor(topAlbumsInteractor: TopAlbumsInteractorImpl): TopAlbumsInteractor

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(TopAlbumsViewModel::class)
    fun bindTopAlbumsViewModel(viewModel: TopAlbumsViewModel): ViewModel
}