package com.test.appsfactorytask.core.di.module.activity

import com.test.appsfactorytask.core.di.module.fragment.BindingFragmentModule
import dagger.Module

@Module(includes = [BindingFragmentModule::class])
interface BaseActivityModule