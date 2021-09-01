package com.test.appsfactorytask.core.api.webservice

import com.test.appsfactorytask.core.api.webservice.response.*
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("?method=artist.gettopalbums")
    suspend fun getTopAlbums(
        @Query("mbid") mbid: String,
        @Query("artist") artist: String
    ): WrapperTopAlbumsResponse

    @GET("?method=album.getinfo")
    suspend fun getTracks(
        @Query("mbid") mbid: String,
        @Query("artist") artist: String,
        @Query("album") album: String
    ): TracksResponse

    @GET("?method=artist.search&page=1")
    suspend fun searchArtists(
        @Query("artist") name: String,
        @Query("limit") limit: Int = 100
    ): SearchResponse
}