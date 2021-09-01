package com.test.appsfactorytask.core.api.repository

import android.net.Uri
import android.util.Log
import com.test.appsfactorytask.core.api.db.DataBase
import com.test.appsfactorytask.core.api.webservice.WebService
import com.test.appsfactorytask.core.api.db.data.Album
import com.test.appsfactorytask.core.api.db.data.toAlbumEntity
import com.test.appsfactorytask.core.api.db.data.toTrackEntity
import com.test.appsfactorytask.core.api.imagesaver.ImageSaver
import com.test.appsfactorytask.core.api.repository.entity.*
import com.test.appsfactorytask.core.api.repository.error.SaveAlbumException
import com.test.appsfactorytask.core.api.webservice.response.*
import com.test.appsfactorytask.core.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataBase: DataBase,
    private val webService: WebService,
    private val imageSaver: ImageSaver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : Repository {

    override val localAlbums: Flow<List<AlbumEntity>> = dataBase
        .localAlbums
        .map { albums ->
            albums.map { album ->
                album.toAlbumEntity()
            }
        }

    override suspend fun loadTracks(albumId: Int): List<TrackEntity> =
        withContext(ioDispatcher) {
            dataBase
                .loadTracks(albumId)
                .map { it.toTrackEntity() }
        }

    override suspend fun loadTopAlbums(artist: ArtistEntity): List<AlbumEntity> =
        withContext(ioDispatcher) {
            val remoteAlbums = webService
                .getTopAlbums(artist.id, artist.name)
                .albums?.map {
                    it.toAlbumEntity()
                } ?: emptyList()
            val localAlbums = dataBase.loadAlbums(artist.id)
            if (localAlbums.isNotEmpty()) {
                remoteAlbums.map {
                    val localAlbum = localAlbums.findAlbum(it)
                    it.copy(id = localAlbum?.id ?: NO_ID, isSaved = localAlbum != null)
                }
            } else {
                remoteAlbums.map {
                    it.copy(authorId = artist.id, author = artist.name)
                }
            }
        }

    override suspend fun loadAlbumInfo(album: AlbumEntity): AlbumEntity =
        withContext(ioDispatcher) {
            val tracks = webService.getTracks(album.mbid, album.author.orEmpty(), album.name).track
            album.copy(tracks = tracks.map { TrackEntity(name = it.name, albumId = album.id) })
        }

    override suspend fun searchArtist(name: String): List<ArtistEntity> =
        withContext(ioDispatcher) {
            webService
                .searchArtists(name)
                .artists?.map {
                    it.toArtistEntity()
                } ?: emptyList()
        }

    override suspend fun saveAlbum(album: AlbumEntity): Int = withContext(ioDispatcher) {
        var albumId = NO_ID
        var imageUri: Uri?
        try {
            imageUri = imageSaver.saveImage(album.image.orEmpty())
            albumId = dataBase.saveAlbum(album.toAlbum().copy(image = imageUri.toString())).toInt()
            if (album.tracks.isNullOrEmpty()) {
                val tracks = webService.getTracks(album.mbid, album.author.orEmpty(), album.name).track
                saveTracks(tracks.map { TrackEntity(it.name, albumId = albumId) })
            } else {
                saveTracks(album.tracks.map { it.copy(albumId = albumId) })
            }
            albumId
        } catch (e: Exception) {
            if (albumId != NO_ID) {
                deleteAlbum(album.copy(id = albumId))
            }
            Log.e(TAG, e.toString())
            throw SaveAlbumException(album.mbid, album.name)
        }
    }

    override suspend fun deleteAlbum(album: AlbumEntity) = withContext(ioDispatcher) {
        imageSaver.deleteImage(album.image.orEmpty())
        dataBase.deleteAlbum(album.toAlbum())
    }

    private suspend fun saveTracks(tracks: List<TrackEntity>) = withContext(ioDispatcher) {
        dataBase.saveTracks(tracks.map { it.toTrack() })
    }

    private fun List<Album>.findAlbum(
        album: AlbumEntity
    ): Album? = firstOrNull {
        (it.mbid.isNotEmpty() && it.mbid == album.mbid) || (it.name == album.name && it.authorId == album.authorId)
    }

    companion object {

        private const val TAG = "RepositoryImpl"

        private const val NO_ID = 0
    }
}