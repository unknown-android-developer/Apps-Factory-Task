package com.test.appsfactorytask.core.api.db

import com.test.appsfactorytask.core.api.db.data.Album
import com.test.appsfactorytask.core.api.db.data.Track
import kotlinx.coroutines.flow.Flow

interface DataBase {

    val localAlbums: Flow<List<Album>>

    suspend fun loadAlbums(artistId: String):List<Album>

    suspend fun loadTracks(albumId: Int): List<Track>

    suspend fun saveAlbum(album: Album): Long

    suspend fun saveTracks(tracks: List<Track>)

    suspend fun deleteAlbum(album: Album)

    suspend fun deleteAlbumById(albumId: Int)
}