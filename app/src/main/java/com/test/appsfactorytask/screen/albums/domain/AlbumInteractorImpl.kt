package com.test.appsfactorytask.screen.albums.domain

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.common.model.toAlbumEntity
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import com.test.appsfactorytask.core.api.repository.entity.toAlbumModel
import com.test.appsfactorytask.core.api.repository.entity.toTrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumInteractorImpl @Inject constructor(private val repository: Repository) : AlbumInteractor {

    override val albums: Flow<List<AlbumModel>>
        get() = repository.localAlbums.map { it.toAlbumModels() }

    override suspend fun loadTracks(album: AlbumModel): List<TrackModel> =
        repository.loadTracks(album.id).map { it.toTrackModel() }

    override suspend fun deleteAlbum(album: AlbumModel) = repository.deleteAlbum(album.toAlbumEntity())

    private fun List<AlbumEntity>.toAlbumModels(): List<AlbumModel> = map { it.toAlbumModel() }
}