package com.test.appsfactorytask.core.api.repository.entity

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.core.api.db.data.Album

data class AlbumEntity(
    val id: Int,
    val mbid: String,
    val name: String,
    val image: String? = null,
    val authorId: String? = null,
    val author: String? = null,
    val tracks: List<TrackEntity>? = null,
    val isSaved: Boolean
)

fun AlbumEntity.toAlbum(): Album = Album(
    id = id,
    mbid = mbid,
    name = name,
    image = image.orEmpty(),
    authorId = authorId.orEmpty(),
    author = author.orEmpty()
)

fun AlbumEntity.toAlbumModel(): AlbumModel = AlbumModel(
    id = id,
    mbid = mbid,
    name = name,
    image = image.orEmpty(),
    authorId = authorId.orEmpty(),
    author = author.orEmpty(),
    tracks = emptyList(),
    isSaved = isSaved
)
