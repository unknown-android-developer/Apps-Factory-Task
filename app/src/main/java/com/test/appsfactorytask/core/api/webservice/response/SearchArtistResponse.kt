package com.test.appsfactorytask.core.api.webservice.response

import com.google.gson.annotations.SerializedName
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import com.test.appsfactorytask.core.api.repository.entity.ImageEntity

data class SearchResponse(val results: ArtistMatchesResponse?)

data class ArtistMatchesResponse(val artistmatches: ArtistMatchResponse?)

data class ArtistMatchResponse(val artist: List<ArtistResponse>?)

data class ArtistResponse(val mbid: String?, val name: String?, val image: List<ImageResponse>?)

data class ImageResponse(@SerializedName("#text") val url: String?, val size: String?)

val SearchResponse.artists: List<ArtistResponse>?
    get() = results?.artistmatches?.artist

fun ArtistResponse.toArtistEntity(): ArtistEntity = ArtistEntity(
    id = mbid.orEmpty(),
    name = name.orEmpty(),
    image = image?.firstOrNull { it.size == "large" }?.toImageEntity()
)

fun ImageResponse.toImageEntity(): ImageEntity? =
    if (url.isNullOrEmpty()) {
        null
    } else {
        ImageEntity(url = url, size = size.orEmpty())
    }