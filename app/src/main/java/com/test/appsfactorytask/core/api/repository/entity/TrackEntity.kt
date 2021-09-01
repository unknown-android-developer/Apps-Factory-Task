package com.test.appsfactorytask.core.api.repository.entity

import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.core.api.db.data.Track

data class TrackEntity(val name: String, val albumId: Int)

fun TrackEntity.toTrack(): Track = Track(
    name = name,
    albumId = albumId
)

fun TrackEntity.toTrackModel(): TrackModel = TrackModel(
    name = name,
    albumId = albumId
)
