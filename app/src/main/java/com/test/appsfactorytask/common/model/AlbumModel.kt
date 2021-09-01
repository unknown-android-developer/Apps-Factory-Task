package com.test.appsfactorytask.common.model

import android.os.Parcelable
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumModel(
    val id: Int,
    val mbid: String,
    val name: String,
    val image: String,
    val authorId: String,
    val author: String,
    val tracks: List<TrackModel>,
    val isSaved: Boolean,
    val isSaving: Boolean = false
) : Parcelable

fun AlbumModel.toAlbumEntity(): AlbumEntity = AlbumEntity(
    id = id,
    mbid = mbid,
    name = name,
    image = image,
    authorId = authorId,
    tracks = tracks.map { it.toTrackEntity() },
    author = author,
    isSaved = isSaved
)

fun TrackModel.toTrackEntity(): TrackEntity = TrackEntity(
    name = name,
    albumId = albumId
)
