package com.test.appsfactorytask.screen.topalbums.domain

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TopAlbumsInteractorImplTest {

    private val repository: Repository = mockk()

    private val interactor: TopAlbumsInteractorImpl = TopAlbumsInteractorImpl(repository)

    private val artistModel = ArtistModel(id = "id", name = "name", image = null)

    private val albumModel = AlbumModel(
        id = 1,
        mbid = "mbid",
        name = "name",
        image = "",
        author = "",
        authorId = "",
        tracks = emptyList(),
        isSaved = false
    )

    @Test
    fun `Test getTopAlbums returns an album`() = runBlockingTest {
        // mock
        val albumEntity = AlbumEntity(
            id = 1,
            mbid = "mbid",
            name = "name",
            isSaved = false
        )
        coEvery { repository.loadTopAlbums(any()) } returns listOf(albumEntity)

        // action
        val albums = interactor.getTopAlbums(artistModel)

        // verify
        assertEquals(albumModel, albums.first())
        coVerify { repository.loadTopAlbums(any()) }
    }

    @Test
    fun `Test getTopAlbums returns empty result`() = runBlockingTest {
        // mock
        coEvery { repository.loadTopAlbums(any()) } returns emptyList()

        // action
        val albums = interactor.getTopAlbums(artistModel)

        // verify
        assertTrue(albums.isEmpty())
        coVerify { repository.loadTopAlbums(any()) }
    }

    @Test
    fun `Test getTracks returns list of tracks`() = runBlockingTest {
        // mock
        val trackEntity = TrackEntity("name", 1)
        val albumEntity = AlbumEntity(
            id = 1,
            mbid = "mbid",
            name = "name",
            tracks = listOf(trackEntity),
            isSaved = false
        )
        coEvery { repository.loadAlbumInfo(any()) } returns albumEntity

        // action
        val tracks = interactor.getTracks(albumModel)

        // verify
        assertEquals(1, tracks.size)
        assertEquals(TrackModel("name", 1), tracks.first())
        coVerify { repository.loadAlbumInfo(any()) }
    }

    @Test
    fun `Test getTracks returns empty result`() = runBlockingTest {
        // mock
        val albumEntity = AlbumEntity(
            id = 1,
            mbid = "mbid",
            name = "name",
            tracks = emptyList(),
            isSaved = false
        )
        coEvery { repository.loadAlbumInfo(any()) } returns albumEntity

        // action
        val tracks = interactor.getTracks(albumModel)

        // verify
        assertTrue(tracks.isEmpty())
        coVerify { repository.loadAlbumInfo(any()) }
    }

    @Test
    fun `Test saveAlbum returns id of saved album`() = runBlockingTest {
        // mock
        coEvery { repository.saveAlbum(any()) } returns 1

        // action
        val albumId = interactor.saveAlbum(albumModel)

        // verify
        assertEquals(1, albumId)
        coVerify { repository.saveAlbum(any()) }
    }

    @Test
    fun `Test deleteAlbum`() = runBlockingTest {
        // mock
        coEvery { repository.deleteAlbum(any()) } just runs

        // action
        interactor.deleteAlbum(albumModel)

        // verify
        coVerify { repository.deleteAlbum(any()) }
    }
}