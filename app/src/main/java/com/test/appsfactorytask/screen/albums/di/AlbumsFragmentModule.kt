package com.test.appsfactorytask.screen.albums.di

import androidx.lifecycle.ViewModel
import com.test.appsfactorytask.core.di.module.viewmodel.ViewModelKey
import com.test.appsfactorytask.core.di.scope.FragmentScope
import com.test.appsfactorytask.screen.albums.domain.AlbumInteractor
import com.test.appsfactorytask.screen.albums.domain.AlbumInteractorImpl
import com.test.appsfactorytask.screen.albums.viewmodel.AlbumsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AlbumsFragmentModule {

    @Binds
    @FragmentScope
    fun bindAlbumInteractor(albumInteractor: AlbumInteractorImpl): AlbumInteractor

    @Binds
    @IntoMap
    @FragmentScope
    @ViewModelKey(AlbumsViewModel::class)
    fun bindAlbumViewModel(viewModel: AlbumsViewModel): ViewModel
}