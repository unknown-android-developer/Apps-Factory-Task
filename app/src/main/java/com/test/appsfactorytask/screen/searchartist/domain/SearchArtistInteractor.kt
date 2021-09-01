package com.test.appsfactorytask.screen.searchartist.domain

import com.test.appsfactorytask.common.model.ArtistModel

interface SearchArtistInteractor {

    suspend fun searchArtist(name: String): List<ArtistModel>
}