package com.test.appsfactorytask.core.di.component

import androidx.lifecycle.ViewModel
import com.test.appsfactorytask.App
import com.test.appsfactorytask.core.di.module.AppModule
import com.test.appsfactorytask.core.di.module.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}