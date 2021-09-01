package com.test.appsfactorytask.screen.topalbums.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.core.api.repository.error.SaveAlbumException
import com.test.appsfactorytask.common.viewmodel.AlbumUiState
import com.test.appsfactorytask.screen.topalbums.domain.TopAlbumsInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopAlbumsViewModel @Inject constructor(
    private val topAlbumsInteractor: TopAlbumsInteractor
) : ViewModel() {

    val uiState: StateFlow<AlbumUiState>
        get() = _uiState

    private val _uiState: MutableStateFlow<AlbumUiState> = MutableStateFlow(
        AlbumUiState.SuccessAlbums(
            emptyList()
        )
    )

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = AlbumUiState.Error(throwable.localizedMessage ?: throwable.toString())
        handleError(throwable)
        Log.e(TAG, throwable.toString())
    }

    private var topAlbums: List<AlbumModel> = emptyList()

    fun onArtist(artist: ArtistModel) = viewModelScope.launch(errorHandler) {
        _uiState.emit(AlbumUiState.Loading)
        topAlbums = topAlbumsInteractor.getTopAlbums(artist)
        _uiState.emit(AlbumUiState.SuccessAlbums(topAlbums))
    }

    fun onSaveClick(album: AlbumModel) = viewModelScope.launch(errorHandler) {
        topAlbums = topAlbums.replaceAlbum(album.copy(isSaved = album.isSaved, isSaving = true))
        _uiState.emit(AlbumUiState.SuccessAlbums(topAlbums))
        if (album.isSaved) {
            topAlbumsInteractor.deleteAlbum(album)
            topAlbums = topAlbums.replaceAlbum(
                album.copy(
                    isSaved = false,
                    isSaving = false
                )
            )
        } else {
            val albumId = topAlbumsInteractor.saveAlbum(album)
            topAlbums = topAlbums.replaceAlbum(
                album.copy(
                    id = albumId,
                    isSaved = true,
                    isSaving = false
                )
            )
        }
        _uiState.emit(AlbumUiState.SuccessAlbums(topAlbums))
    }

    fun onAlbumClick(album: AlbumModel) = viewModelScope.launch(errorHandler) {
        if (album.tracks.isEmpty()) {
            _uiState.emit(AlbumUiState.Loading)
            val tracks = topAlbumsInteractor.getTracks(album)
            val albumWithTracks = album.copy(tracks = tracks)
            topAlbums = topAlbums.replaceAlbum(albumWithTracks)
            _uiState.emit(AlbumUiState.SuccessTracks(albumWithTracks))
            _uiState.emit(AlbumUiState.SuccessAlbums(topAlbums))
        } else {
            _uiState.emit(AlbumUiState.SuccessTracks(album))
        }
    }

    private fun handleError(throwable: Throwable) {
        (throwable as? SaveAlbumException)?.let {
            topAlbums = topAlbums.setAlbumNotSaved(it.mbid, it.name)
            _uiState.value = AlbumUiState.SuccessAlbums(topAlbums)
            _uiState.value = AlbumUiState.Error("Could not save album ${it.name}")
        }
    }

    private fun List<AlbumModel>.replaceAlbum(
        album: AlbumModel
    ): List<AlbumModel> = map {
        if (album.id != NO_ID && album.id == it.id || (album.mbid == it.mbid && album.name == it.name)) {
            album
        } else {
            it
        }
    }

    private fun List<AlbumModel>.setAlbumNotSaved(
        mbid: String,
        name: String
    ): List<AlbumModel> = map {
        if (it.mbid == mbid && it.name == name) {
            it.copy(isSaved = false, isSaving = false)
        } else {
            it
        }
    }

    companion object {

        private const val NO_ID = 0

        private const val TAG = "TopAlbumsViewModel"
    }
}