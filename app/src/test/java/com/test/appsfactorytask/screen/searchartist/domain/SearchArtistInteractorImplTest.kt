package com.test.appsfactorytask.screen.searchartist.domain

import com.test.appsfactorytask.common.model.ArtistModel
import com.test.appsfactorytask.common.model.ImageModel
import com.test.appsfactorytask.core.api.repository.Repository
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import com.test.appsfactorytask.core.api.repository.entity.ImageEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchArtistInteractorImplTest {

    private val repository: Repository = mockk()

    private val interactor: SearchArtistInteractorImpl = SearchArtistInteractorImpl(repository)

    @ExperimentalCoroutinesApi
    @Test
    fun `Test searchArtist artist found`() = runBlockingTest {
        // mock
        val imageEntity = ImageEntity("url", "size")
        val artistEntity = ArtistEntity(id = "id", name = "name", image = imageEntity)
        val imageModel = ImageModel("url", "size")
        coEvery { repository.searchArtist("artists name") } returns listOf(artistEntity)

        // verify
        val artists = interactor.searchArtist("artists name")

        // verify
        assertTrue(artists.isNotEmpty())
        assertEquals(1, artists.size)
        assertEquals(ArtistModel(id = "id", name = "name", image = imageModel), artists.first())
        coVerify(exactly = 1) { repository.searchArtist("artists name") }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test searchArtist artist not found`() = runBlockingTest {
        // mock
        coEvery { repository.searchArtist("artists name") } returns emptyList()

        // verify
        val artists = interactor.searchArtist("artists name")

        // verify
        assertTrue(artists.isEmpty())
        coVerify(exactly = 1) { repository.searchArtist("artists name") }
    }
}