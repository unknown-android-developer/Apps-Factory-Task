package com.test.appsfactorytask.screen.albums.viewmodel

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.common.viewmodel.AlbumUiState
import com.test.appsfactorytask.screen.albums.domain.AlbumInteractor
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumsViewModelTest {

    private val interactor: AlbumInteractor = mockk()

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: AlbumsViewModel

    private val album: AlbumModel = AlbumModel(
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
        Dispatchers.setMain(testDispatcher)
        every { interactor.albums } returns flow { emit(listOf(album)) }
        viewModel = AlbumsViewModel(interactor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test getUiState returns last loaded albums`() =  runBlockingTest {
        // verify
        (viewModel.uiState.value as AlbumUiState.SuccessAlbums).let {
            assertEquals(1, it.albums.size)
            assertEquals(album, it.albums.first())
        }
        coVerify { interactor.albums }
    }

    @Test
    fun `Test onDeleteAlbum`() {
        // mock
        coEvery { interactor.deleteAlbum(album) } just runs

        // action
        viewModel.onDeleteAlbum(album)

        // verify
        coVerify { interactor.deleteAlbum(album) }
    }

    @Test
    fun `Test onAlbumClick track list loaded`() = runBlockingTest {
        // mock
        val trackModel = TrackModel("name", 1)
        coEvery { interactor.loadTracks(album) } returns listOf(trackModel)

        // action
        viewModel.onAlbumClick(album)

        // verify
        (viewModel.uiState.value as AlbumUiState.SuccessTracks).let {
            assertEquals(1, it.tracks.tracks.size)
            assertEquals(trackModel, it.tracks.tracks.first())
        }
        coVerify { interactor.loadTracks(album) }
    }

    @Test
    fun `Test onAlbumClick track list is not loaded`() = runBlockingTest {
        // mock
        val trackModel = TrackModel("name", 1)

        // action
        viewModel.onAlbumClick(album.copy(tracks = listOf(trackModel)))

        // verify
        (viewModel.uiState.value as AlbumUiState.SuccessTracks).let {
            assertEquals(1, it.tracks.tracks.size)
            assertEquals(trackModel, it.tracks.tracks.first())
        }
        coVerify(exactly = 0) { interactor.loadTracks(album) }
    }
}