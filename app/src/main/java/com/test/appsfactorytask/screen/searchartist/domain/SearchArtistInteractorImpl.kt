package com.test.appsfactorytask.screen.searchartist.domain

import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import com.test.appsfactorytask.core.api.repository.entity.ImageEntity
import com.test.appsfactorytask.common.model.ImageModel
import com.test.appsfactorytask.common.model.ArtistModel
import javax.inject.Inject

class SearchArtistInteractorImpl @Inject constructor(
    private val repository: Repository
) : SearchArtistInteractor {

    override suspend fun searchArtist(name: String): List<ArtistModel> = repository.searchArtist(name).map { it.toArtistModel() }

    private fun ArtistEntity.toArtistModel(): ArtistModel = ArtistModel(
        id = id,
        name = name,
        image = image?.toImageModel()
    )

    private fun ImageEntity.toImageModel(): ImageModel = ImageModel(
        url = url,
        size = size
    )
}