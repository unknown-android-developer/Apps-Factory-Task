package com.test.appsfactorytask.screen.albums.domain

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumInteractorImplTest {

    private val repository: Repository = mockk()

    private val interactor: AlbumInteractorImpl = AlbumInteractorImpl(repository)

    private val albumEntity: AlbumEntity = AlbumEntity(
        id = 1,
        mbid = "mbid",
        name = "name",
        image = "image",
        authorId = "authorId",
        author = "author",
        tracks = emptyList(),
        isSaved = true
    )

    private val albumModel: AlbumModel = AlbumModel(
        id = 1,
        mbid = "mbid",
        name = "name",
        image = "image",
        authorId = "authorId",
        author = "author",
        tracks = emptyList(),
        isSaved = true
    )

    @Before
    fun setUp() {
        every { repository.localAlbums } returns flow { emit(listOf(albumEntity)) }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Test albums emits 1 item`() = runBlockingTest {
        // action + verify
        interactor.albums.collect {
            assertEquals(1, it.size)
            assertEquals(albumModel, it.first())
        }
    }

    @Test
    fun `Test loadTracks returns 1 track`() = runBlockingTest {
        // mock
        val trackEntity = mockk<TrackEntity>()
        every { trackEntity.name } returns "name"
        every { trackEntity.albumId } returns 1
        coEvery { repository.loadTracks(1) } returns listOf(trackEntity)

        // action
        val tracks = interactor.loadTracks(albumModel)

        // verify
        assertTrue(tracks.isNotEmpty())
        assertEquals(1, tracks.size)
        assertEquals(TrackModel("name", 1), tracks.first())
        coVerify { repository.loadTracks(1) }
    }

    @Test
    fun deleteAlbum() = runBlockingTest {
        // mock
        coEvery { repository.deleteAlbum(any()) } just runs

        // action
        interactor.deleteAlbum(albumModel)

        // verify
        coVerify { repository.deleteAlbum(any()) }
    }
}