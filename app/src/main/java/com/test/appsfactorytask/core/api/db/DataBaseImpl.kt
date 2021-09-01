package com.test.appsfactorytask.core.api.db

import com.test.appsfactorytask.core.api.db.data.Album
import com.test.appsfactorytask.core.api.db.data.Track
import com.test.appsfactorytask.core.api.db.room.AlbumDao
import com.test.appsfactorytask.core.api.db.room.AppDatabase
import com.test.appsfactorytask.core.api.db.room.TrackDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataBaseImpl @Inject constructor(appDatabase: AppDatabase) : DataBase {

    override val localAlbums: Flow<List<Album>>
        get() = albumDao.getAllAlbums()

    private val albumDao: AlbumDao = appDatabase.albumDao()

    private val trackDao: TrackDao = appDatabase.trackDao()

    override suspend fun loadAlbums(artistId: String): List<Album> = albumDao.getAlbumsByAuthor(artistId)

    override suspend fun loadTracks(albumId: Int): List<Track> = trackDao.getTracksByAlbum(albumId)

    override suspend fun saveAlbum(album: Album): Long = albumDao.insertAlbum(album)

    override suspend fun saveTracks(tracks: List<Track>) = trackDao.insertTracks(tracks)

    override suspend fun deleteAlbum(album: Album) {
        albumDao.deleteAlbum(album)
        trackDao.deleteTracksByAlbum(album.id)
    }

    override suspend fun deleteAlbumById(albumId: Int) {
        albumDao.deleteAlbumById(albumId)
        trackDao.deleteTracksByAlbum(albumId)
    }
}