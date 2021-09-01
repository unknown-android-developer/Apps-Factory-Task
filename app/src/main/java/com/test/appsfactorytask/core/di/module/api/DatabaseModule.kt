package com.test.appsfactorytask.core.di.module.api

import android.content.Context
import androidx.room.Room
import com.test.appsfactorytask.core.api.db.room.AppDatabase
import com.test.appsfactorytask.core.di.qualifier.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    private const val DB_NAME = "app-database"

    @Provides
    @Singleton
    fun provideDB(@AppContext appContext: Context): AppDatabase =
        Room
            .databaseBuilder(appContext, AppDatabase::class.java, DB_NAME)
            .build()
}