package com.test.appsfactorytask.core.api.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity

@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "mbid") val mbid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "authorId") val authorId: String,
    @ColumnInfo(name = "author") val author: String
)

fun Album.toAlbumEntity(): AlbumEntity = AlbumEntity(
    id = id,
    mbid = mbid,
    name = name,
    image = image,
    authorId = authorId,
    author = author,
    tracks = emptyList(),
    isSaved = true
)
