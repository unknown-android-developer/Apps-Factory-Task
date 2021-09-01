package com.test.appsfactorytask.core.api.webservice.response

import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity

private const val NO_ID = 0

data class WrapperTopAlbumsResponse(val topalbums: TopAlbumsResponse?)

data class TopAlbumsResponse(val album: List<AlbumResponse>?)

data class AlbumResponse(
    val mbid: String?,
    val name: String?,
    val artist: ArtistResponse?,
    val image: List<ImageResponse>?
)

val WrapperTopAlbumsResponse.albums: List<AlbumResponse>?
    get() = topalbums?.album

fun AlbumResponse.toAlbumEntity(): AlbumEntity = AlbumEntity(
    id = NO_ID,
    mbid = mbid.orEmpty(),
    name = name.orEmpty(),
    image = image?.firstOrNull { it.size == "large" }?.url,
    authorId = artist?.mbid,
    author = artist?.name,
    tracks = emptyList(),
    isSaved = false
)

