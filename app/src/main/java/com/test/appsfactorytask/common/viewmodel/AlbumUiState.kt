package com.test.appsfactorytask.common.viewmodel

import com.test.appsfactorytask.common.model.AlbumModel

sealed class AlbumUiState {

    object Loading : AlbumUiState()

    class SuccessAlbums(val albums: List<AlbumModel>) : AlbumUiState()

    class SuccessTracks(val tracks: AlbumModel) : AlbumUiState()

    class Error(val error: String) : AlbumUiState()
}
