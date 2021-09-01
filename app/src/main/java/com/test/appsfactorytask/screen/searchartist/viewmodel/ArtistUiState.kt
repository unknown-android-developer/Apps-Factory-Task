package com.test.appsfactorytask.screen.searchartist.viewmodel

import com.test.appsfactorytask.common.model.ArtistModel

sealed class ArtistUiState {

    object Idle : ArtistUiState()

    object Loading : ArtistUiState()

    class Success(val artists: List<ArtistModel>) : ArtistUiState()

    class Error(val error: String) : ArtistUiState()
}
