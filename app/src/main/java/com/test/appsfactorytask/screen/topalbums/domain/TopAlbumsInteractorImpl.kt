package com.test.appsfactorytask.screen.topalbums.domain

import com.test.appsfactorytask.common.model.*
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.*
import javax.inject.Inject

class TopAlbumsInteractorImpl @Inject constructor(
    private val repository: Repository
) : TopAlbumsInteractor {

    override suspend fun getTopAlbums(artist: ArtistModel): List<AlbumModel> =
        repository
            .loadTopAlbums(artist.toArtistEntity())
            .map { it.toAlbumModel() }

    override suspend fun getTracks(album: AlbumModel): List<TrackModel> =
        repository
            .loadAlbumInfo(album.toAlbumEntity())
            .tracks?.map {
                it.toTrackModel()
            } ?: emptyList()

    override suspend fun saveAlbum(album: AlbumModel): Int = repository.saveAlbum(album.toAlbumEntity())

    override suspend fun deleteAlbum(album: AlbumModel) = repository.deleteAlbum(album.toAlbumEntity())
}