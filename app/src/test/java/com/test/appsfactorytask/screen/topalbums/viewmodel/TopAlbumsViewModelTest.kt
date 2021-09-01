package com.test.appsfactorytask.screen.topalbums.viewmodel

import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.common.model.TrackModel
import com.test.appsfactorytask.common.viewmodel.AlbumUiState
import com.test.appsfactorytask.screen.topalbums.domain.TopAlbumsInteractor
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TopAlbumsViewModelTest {

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private val interactor: TopAlbumsInteractor = mockk()

    private val viewModel: TopAlbumsViewModel = TopAlbumsViewModel(interactor)

    private val albumModel = AlbumModel(
        id = 0,
        mbid = "mbid",
        name = "name",
        image = "",
        author = "",
        authorId = "",
        tracks = emptyList(),
        isSaved = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test uiState contains initial state`() {
        // action
        val uiState = viewModel.uiState.value

        // verify
        assertTrue(uiState is AlbumUiState.SuccessAlbums)
        assertEquals(emptyList<AlbumModel>(), (uiState as AlbumUiState.SuccessAlbums).albums)
    }

    @Test
    fun `Test onArtist loads the artists top albums`() = runBlockingTest {
        // mock
        val artistModel = ArtistModel("id", "name", null)
        coEvery { interactor.getTopAlbums(artistModel) } returns listOf(albumModel)

        // action
        val uiStates = mutableListOf<AlbumUiState>()
        val job = launch { viewModel.uiState.toList(uiStates) }
        viewModel.onArtist(artistModel)

        // verify
        assertTrue(uiStates[0] is AlbumUiState.SuccessAlbums)
        assertTrue(uiStates[1] is AlbumUiState.Loading)
        assertTrue(uiStates[2] is AlbumUiState.SuccessAlbums)
        (viewModel.uiState.value as AlbumUiState.SuccessAlbums).let {
            assertEquals(1, it.albums.size)
            assertEquals(albumModel, it.albums.first())
        }
        coVerify { interactor.getTopAlbums(artistModel) }
        job.cancel()
    }

    @Test
    fun `Test onSaveClick saves album`() = runBlockingTest {
        // mock
        val artistModel = ArtistModel("id", "name", null)
        coEvery { interactor.getTopAlbums(artistModel) } returns listOf(albumModel)
        coEvery { interactor.saveAlbum(albumModel) } returns 1

        // action
        viewModel.onArtist(artistModel)
        viewModel.onSaveClick(albumModel)

        // verify
        (viewModel.uiState.value as AlbumUiState.SuccessAlbums).let {
            assertEquals(1, it.albums.size)
            assertEquals(1, it.albums.first().id)
            assertFalse(it.albums.first().isSaving)
            assertTrue(it.albums.first().isSaved)
        }
        coVerify { interactor.saveAlbum(albumModel) }
    }

    @Test
    fun `Test onSaveClick deletes album`() = runBlockingTest {
        // mock
        val artistModel = ArtistModel("id", "name", null)
        val savedAlbumModel = albumModel.copy(isSaved = true)
        coEvery { interactor.getTopAlbums(artistModel) } returns listOf(savedAlbumModel)
        coEvery { interactor.deleteAlbum(savedAlbumModel) } just runs

        // action
        viewModel.onArtist(artistModel)
        viewModel.onSaveClick(savedAlbumModel)

        // verify
        (viewModel.uiState.value as AlbumUiState.SuccessAlbums).let {
            assertEquals(1, it.albums.size)
            assertFalse(it.albums.first().isSaving)
            assertFalse(it.albums.first().isSaved)
        }
        coVerify { interactor.deleteAlbum(savedAlbumModel) }
    }

    @Test
    fun `Test onAlbumClick loads tracks`() = runBlockingTest {
        // mock
        val trackModel = TrackModel("name", 1)
        coEvery { interactor.getTracks(albumModel) } returns listOf(trackModel)

        // action
        val uiStates = mutableListOf<AlbumUiState>()
        val job = launch { viewModel.uiState.toList(uiStates) }
        viewModel.onAlbumClick(albumModel)

        // verify
        assertEquals(4, uiStates.size)
        assertTrue(uiStates[0] is AlbumUiState.SuccessAlbums)
        assertTrue(uiStates[1] is AlbumUiState.Loading)
        (uiStates[2] as AlbumUiState.SuccessTracks).let {
            assertEquals(1, it.tracks.tracks.size)
            assertEquals(trackModel, it.tracks.tracks.first())
        }
        assertTrue(uiStates[0] is AlbumUiState.SuccessAlbums)
        coVerify { interactor.getTracks(albumModel) }
        job.cancel()
    }
}