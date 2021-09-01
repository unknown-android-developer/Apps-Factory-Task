package com.test.appsfactorytask.screen.topalbums.domain

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.common.model.TrackModel

interface TopAlbumsInteractor {

    suspend fun getTopAlbums(artist: ArtistModel): List<AlbumModel>

    suspend fun getTracks(album: AlbumModel): List<TrackModel>

    suspend fun saveAlbum(album: AlbumModel): Int

    suspend fun deleteAlbum(album: AlbumModel)
}