package com.test.appsfactorytask.screen.searchartist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.appsfactorytask.screen.searchartist.domain.SearchArtistInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchArtistViewModel @Inject constructor(
    private val searchArtistInteractor: SearchArtistInteractor
) : ViewModel() {

    val uiState: StateFlow<ArtistUiState>
        get() = _uiState

    private val _uiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState.Idle)

    private var query: String = ""

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = ArtistUiState.Error(throwable.localizedMessage ?: throwable.toString())
        Log.e(TAG, throwable.toString())
    }

    fun onTextChange(text: String) {
        query = text
    }

    fun onSearch() = viewModelScope.launch(errorHandler) {
        if (query.isNotEmpty()) {
            _uiState.emit(ArtistUiState.Loading)
            val artists = searchArtistInteractor.searchArtist(query)
            _uiState.emit(ArtistUiState.Success(artists))
        }
    }

    companion object {

        private const val TAG = "SearchArtisViewModel"
    }
}