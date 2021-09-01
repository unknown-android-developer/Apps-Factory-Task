package com.test.appsfactorytask.common.model

import android.os.Parcelable
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistModel(
    val id: String,
    val name: String,
    val image: ImageModel?
) : Parcelable

fun ArtistModel.toArtistEntity(): ArtistEntity = ArtistEntity(
    id = id,
    name = name,
    image = null
)
