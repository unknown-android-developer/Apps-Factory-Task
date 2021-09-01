package com.test.appsfactorytask.screen.albums.domain

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface AlbumInteractor {

    val albums: Flow<List<AlbumModel>>

    suspend fun loadTracks(album: AlbumModel): List<TrackModel>

    suspend fun deleteAlbum(album: AlbumModel)
}