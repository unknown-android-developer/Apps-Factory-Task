package com.test.appsfactorytask.core.api.repository

import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    val localAlbums: Flow<List<AlbumEntity>>

    suspend fun loadTracks(albumId: Int): List<TrackEntity>

    suspend fun loadTopAlbums(artist: ArtistEntity): List<AlbumEntity>

    suspend fun loadAlbumInfo(album: AlbumEntity): AlbumEntity

    suspend fun searchArtist(name: String): List<ArtistEntity>

    suspend fun saveAlbum(album: AlbumEntity): Int

    suspend fun deleteAlbum(album: AlbumEntity)
}