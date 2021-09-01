package com.test.appsfactorytask.core.api.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity

@Entity(tableName = "track")
data class Track(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "albumId") val albumId: Int
)

fun Track.toTrackEntity(): TrackEntity = TrackEntity(
    name = name,
    albumId = albumId
)
