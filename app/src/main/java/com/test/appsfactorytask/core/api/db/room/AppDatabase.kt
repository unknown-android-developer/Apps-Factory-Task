package com.test.appsfactorytask.core.api.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.appsfactorytask.core.api.db.data.Album
import com.test.appsfactorytask.core.api.db.data.Track

@Database(entities = [Album::class, Track::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    abstract fun trackDao(): TrackDao
}