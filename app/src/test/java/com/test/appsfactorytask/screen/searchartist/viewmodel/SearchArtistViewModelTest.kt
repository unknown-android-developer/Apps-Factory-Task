package com.test.appsfactorytask.screen.searchartist.viewmodel

import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.screen.searchartist.domain.SearchArtistInteractor
import io.mockk.*
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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
class SearchArtistViewModelTest {

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private val interator: SearchArtistInteractor = mockk()

    private lateinit var viewModel: SearchArtistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel  = SearchArtistViewModel(interator)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test uiState contains Idle state`() {
        // action + verify
        assertEquals(ArtistUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `Text onTextChange should not start search`() {
        // action
        viewModel.onTextChange("text")

        // verify
        assertEquals(ArtistUiState.Idle, viewModel.uiState.value)
        coVerify { interator wasNot called }
    }

    @Test
    fun `Test onSearch should not start search`() {
        // action
        viewModel.onSearch()

        // verify
        assertEquals(ArtistUiState.Idle, viewModel.uiState.value)
        verify { interator wasNot called }
    }

    @Test
    fun `Test onSearch should find an artist`() = runBlockingTest {
        // mock
        val artistModel = ArtistModel("id", "name", null)
        coEvery { interator.searchArtist("text") } returns listOf(artistModel)

        // action
        val uiStates = mutableListOf<ArtistUiState>()
        viewModel.onTextChange("text")
        viewModel.onSearch()
        val job = launch { viewModel.uiState.toList(uiStates) }

        // verify
        (viewModel.uiState.value as ArtistUiState.Success).let {
            assertTrue(it.artists.isNotEmpty())
            assertEquals(1, it.artists.size)
            assertEquals(artistModel, it.artists.first())
        }
        coVerify { interator.searchArtist("text") }
        job.cancel()
    }

    @Test
    fun `Test onSearch should find nothing`() = runBlockingTest {
        // mock
        val artistModel = ArtistModel("id", "name", null)
        coEvery { interator.searchArtist("text") } returns emptyList()

        // action
        val uiStates = mutableListOf<ArtistUiState>()
        viewModel.onTextChange("text")
        viewModel.onSearch()
        val job = launch { viewModel.uiState.toList(uiStates) }

        // verify
        (viewModel.uiState.value as ArtistUiState.Success).let {
            assertTrue(it.artists.isEmpty())
        }
        coVerify { interator.searchArtist("text") }
        job.cancel()
    }
}