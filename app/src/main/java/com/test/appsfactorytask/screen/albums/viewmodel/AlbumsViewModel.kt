package com.test.appsfactorytask.screen.albums.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.viewmodel.AlbumUiState
import com.test.appsfactorytask.core.di.qualifier.MainDispatcher
import com.test.appsfactorytask.screen.albums.domain.AlbumInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AlbumsViewModel @Inject constructor(
    private val albumInteractor: AlbumInteractor
) : ViewModel() {

    val uiState: StateFlow<AlbumUiState>
        get() = _uiState

    private val _uiState: MutableStateFlow<AlbumUiState> = MutableStateFlow(
        AlbumUiState.SuccessAlbums(emptyList())
    )

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = AlbumUiState.Error(throwable.localizedMessage ?: throwable.toString())
        Log.e(TAG, throwable.toString())
    }

    init {
        viewModelScope.launch(errorHandler) {
            albumInteractor.albums.collect {
                _uiState.value = AlbumUiState.SuccessAlbums(it)
            }
        }
    }

    fun onDeleteAlbum(album: AlbumModel) = viewModelScope.launch(errorHandler) {
        albumInteractor.deleteAlbum(album)
    }

    fun onAlbumClick(album: AlbumModel) = viewModelScope.launch(errorHandler) {
        _uiState.emit(AlbumUiState.Loading)
        if (album.tracks.isEmpty()) {
            val tracks = albumInteractor.loadTracks(album)
            _uiState.emit(AlbumUiState.SuccessTracks(album.copy(tracks = tracks)))
        } else {
            _uiState.emit(AlbumUiState.SuccessTracks(album.copy()))
        }
    }

    companion object {
        private const val TAG = "AlbumsViewModel"
    }
}