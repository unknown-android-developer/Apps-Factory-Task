package com.test.appsfactorytask

import android.app.Application
import com.test.appsfactorytask.core.di.component.AppComponent
import com.test.appsfactorytask.core.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var activityLifecycleCallbacks: ActivityLifecycleCallbacks

    override fun onCreate() {
        super.onCreate()
        appComponent().inject(this)
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun appComponent(): AppComponent = DaggerAppComponent
        .builder()
        .app(this)
        .build()
}