package com.test.appsfactorytask.core.di.module.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.test.appsfactorytask.core.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}