package com.test.appsfactorytask.core.di.module

import android.app.Application
import android.content.Context
import com.test.appsfactorytask.App
import com.test.appsfactorytask.core.api.db.DataBase
import com.test.appsfactorytask.core.api.db.DataBaseImpl
import com.test.appsfactorytask.core.api.imagesaver.ImageSaver
import com.test.appsfactorytask.core.api.imagesaver.ImageSaverImpl
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.RepositoryImpl
import com.test.appsfactorytask.core.di.module.api.DatabaseModule
import com.test.appsfactorytask.core.di.module.api.WebServiceModule
import com.test.appsfactorytask.core.di.module.activity.ActivityBindingModule
import com.test.appsfactorytask.core.di.qualifier.AppContext
import com.test.appsfactorytask.core.view.lifecycle.ActivityLifecycleCallbacksImpl
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [
    AndroidInjectionModule::class,
    ActivityBindingModule::class,
    WebServiceModule::class,
    DatabaseModule::class,
    DispatcherModule::class
])
interface AppModule {

    @Binds
    @Singleton
    @AppContext
    fun bindAppContext(app: App): Context

    @Binds
    @Singleton
    fun bindActivityLifecycleCallbacks(
        activityLifeCycleCallBack: ActivityLifecycleCallbacksImpl
    ): Application.ActivityLifecycleCallbacks

    @Binds
    @Singleton
    fun bindRepository(repository: RepositoryImpl): Repository

    @Binds
    @Singleton
    fun bindDatabase(database: DataBaseImpl): DataBase

    @Binds
    @Singleton
    fun bindImageSaver(imageSaver: ImageSaverImpl): ImageSaver
}