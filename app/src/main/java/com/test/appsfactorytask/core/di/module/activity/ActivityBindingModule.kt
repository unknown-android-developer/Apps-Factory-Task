package com.test.appsfactorytask.core.di.module.activity

import com.test.appsfactorytask.MainActivity
import com.test.appsfactorytask.core.di.module.fragment.BindingFragmentModule
import com.test.appsfactorytask.core.di.module.viewmodel.ViewModelModule
import com.test.appsfactorytask.core.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [BindingFragmentModule::class, MainActivityModule::class, ViewModelModule::class])
    fun mainActivityInjector(): MainActivity
}