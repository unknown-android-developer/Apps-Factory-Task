package com.test.appsfactorytask.core.api.repository

import android.net.Uri
import android.util.Log
import com.test.appsfactorytask.core.api.db.DataBase
import com.test.appsfactorytask.core.api.db.data.Album
import com.test.appsfactorytask.core.api.db.data.Track
import com.test.appsfactorytask.core.api.imagesaver.ImageSaver
import com.test.appsfactorytask.core.api.repository.entity.AlbumEntity
import com.test.appsfactorytask.core.api.repository.entity.ArtistEntity
import com.test.appsfactorytask.core.api.repository.entity.ImageEntity
import com.test.appsfactorytask.core.api.repository.entity.TrackEntity
import com.test.appsfactorytask.core.api.webservice.WebService
import com.test.appsfactorytask.core.api.webservice.response.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private val db: DataBase = mockk()

    private val webService: WebService = mockk()

    private val imageSaver: ImageSaver = mockk()

    private lateinit var repository: RepositoryImpl

    private val imageResponse = mockk<ImageResponse>(relaxed = true)

    private val album: Album = Album(
        id = 1,
        mbid = "mbid",
        name = "name",
        image = "image",
        authorId = "authorId",
        author = "author"
    )

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

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e("RepositoryImpl", any()) } returns 0
        every { db.localAlbums } returns flow { emit(listOf(album)) }
        every { imageResponse.url } returns "image"
        every { imageResponse.size } returns "large"
        repository = RepositoryImpl(
            dataBase = db,
            webService = webService,
            imageSaver,
            testDispatcher
        )
    }

    @Test
    fun `Test localAlbum flow returns data`() = runBlockingTest {
        // action + verify
        repository.localAlbums.collect {
            assertEquals(1, it.size)
            assertEquals(albumEntity, it.first())
        }
    }

    @Test
    fun `Test load tracks from local db`() = runBlockingTest {
        // mock
        val track = mockk<Track>()
        every { track.id } returns 1
        every { track.albumId } returns 1
        every { track.name } returns "track"
        coEvery { db.loadTracks(1) } returns listOf(track)

        // action
        val tracks = repository.loadTracks(1)

        // verify
        assertTrue(tracks.isNotEmpty())
        assertEquals(1, tracks.size)
        assertEquals(TrackEntity("track", 1), tracks.first())
        coVerify { db.loadTracks(1) }
    }

    @Test
    fun `Test load top albums the albums not saved`() = runBlockingTest {
        // mock
        val artist = mockk<ArtistEntity>(relaxed = true)
        val topAlbumResponse = prepareMockTopAlbumResponse()
        every { artist.id } returns "id"
        every { artist.name } returns "artist"
        coEvery { webService.getTopAlbums("id", "artist") } returns topAlbumResponse
        coEvery { db.loadAlbums("id") } returns emptyList()

        // action
        val album = repository.loadTopAlbums(artist).firstOrNull()

        // verify
        assertNotNull(album)
        assertTrue(album?.isSaved == false)
        coVerify(exactly = 1) { webService.getTopAlbums("id", "artist") }
        coVerify(exactly = 1) { db.loadAlbums("id") }
    }

    @Test
    fun `Test load top albums the albums already saved`() = runBlockingTest {
        // mock
        val artist = mockk<ArtistEntity>(relaxed = true)
        val topAlbumResponse = prepareMockTopAlbumResponse()
        every { artist.id } returns "id"
        every { artist.name } returns "artist"
        coEvery { webService.getTopAlbums("id", "artist") } returns topAlbumResponse
        coEvery { db.loadAlbums("id") } returns listOf(album)

        // action
        val album = repository.loadTopAlbums(artist).firstOrNull()

        // verify
        assertNotNull(album)
        assertEquals(albumEntity, album)
        assertEquals(true, album?.isSaved)
        coVerify(exactly = 1) { webService.getTopAlbums("id", "artist") }
        coVerify(exactly = 1) { db.loadAlbums("id") }
    }

    @Test
    fun `Test loadAlbumInfo returns albums with tracks`() = runBlockingTest {
        // mock
        val tracksResponse = prepareMockTracksResponse()
        coEvery { webService.getTracks("mbid", "author", "name") } returns tracksResponse

        // action
        val album = repository.loadAlbumInfo(albumEntity)

        // verify
        assertTrue(album.tracks?.isNotEmpty() == true)
        assertEquals("track name", album.tracks?.first()?.name)
        assertEquals(albumEntity.id, album.tracks?.first()?.albumId)
        coVerify(exactly = 1) { webService.getTracks("mbid", "author", "name") }
    }

    @Test
    fun `Test searchArtist returns not empty list`() = runBlockingTest {
        // mock
        val searchResponse = prepareMockSearchArtistResponse()
        coEvery { webService.searchArtists("artist") } returns searchResponse

        // verify
        val artists = repository.searchArtist("artist")

        // action
        assertTrue(artists.isNotEmpty())
        artists.first().apply {
            assertEquals("mbid", id)
            assertEquals("name", name)
            assertEquals(ImageEntity("image", "large"), image)
        }
        coVerify(exactly = 1) { webService.searchArtists("artist") }
    }

    @Test
    fun `Test searchArtist returns empty list`() = runBlockingTest {
        // mock
        val searchResponse = prepareMockSearchArtistResponse()
        every { searchResponse.results?.artistmatches?.artist } returns emptyList()
        coEvery { webService.searchArtists("artist") } returns searchResponse

        // action
        val artists = repository.searchArtist("artist")

        // verify
        assertTrue(artists.isEmpty())
        coVerify(exactly = 1) { webService.searchArtists("artist") }
    }

    @Test
    fun `Test saveAlbum tracks not loaded`() = runBlockingTest {
        // mock
        mockkStatic(Uri::class)
        val uri = mockk<Uri>(relaxed = true)
        val tracksResponse = prepareMockTracksResponse()
        every { uri.toString() } returns "uri"
        coEvery { imageSaver.saveImage("image") } returns uri
        coEvery { db.saveAlbum(any()) } returns 1
        coEvery { webService.getTracks("mbid", "author", "name") } returns tracksResponse
        coEvery { db.saveTracks(any()) } just runs

        // action
        val albumId = repository.saveAlbum(albumEntity)

        // verify
        assertEquals(1, albumId)
        coVerify(exactly = 1) { db.saveAlbum(any()) }
        coVerify(exactly = 1) { db.saveTracks(any()) }
        coVerify(exactly = 0) { db.deleteAlbumById(any()) }
        coVerify(exactly = 0) { imageSaver.deleteImage(any()) }
        coVerifyAll {
            imageSaver.saveImage("image")
            webService.getTracks("mbid", "author", "name")
        }
    }

    @Test
    fun `Test saveAlbum tracks loaded`() = runBlockingTest {
        // mock
        mockkStatic(Uri::class)
        val uri = mockk<Uri>(relaxed = true)
        every { uri.toString() } returns "uri"
        coEvery { imageSaver.saveImage("image") } returns uri
        coEvery { db.saveAlbum(any()) } returns 1
        coEvery { db.saveTracks(any()) } just runs

        // action
        val albumId = repository.saveAlbum(albumEntity.copy(tracks = listOf(TrackEntity("track", 1))))

        // verify
        assertEquals(1, albumId)
        coVerify(exactly = 1) { db.saveAlbum(any()) }
        coVerify(exactly = 1) { db.saveTracks(any()) }
        coVerify(exactly = 1) { imageSaver.saveImage("image") }
        coVerify(exactly = 0) { db.deleteAlbumById(any()) }
        coVerify(exactly = 0) { imageSaver.deleteImage(any()) }
        coVerify(exactly = 0) { webService.getTracks(any(), any(), any()) }
    }

    @Test
    fun `Test deleteAlbum`() = runBlockingTest {
        // mock
        coEvery { imageSaver.deleteImage(any()) } just runs
        coEvery { db.deleteAlbum(any()) } just runs

        // action
        repository.deleteAlbum(albumEntity)

        // verify
        coVerify { imageSaver.deleteImage(any()) }
        coVerify { db.deleteAlbum(any()) }
    }

    private fun prepareMockTracksResponse(): TracksResponse =
        mockk<TracksResponse>(relaxed = true).apply {
            val trackResponse = mockk<TrackResponse>()
            every { track } returns listOf(trackResponse)
            every { trackResponse.name } returns "track name"
        }

    private fun prepareMockTopAlbumResponse(): WrapperTopAlbumsResponse =
        mockk<WrapperTopAlbumsResponse>(relaxed = true).apply {
            val topAlbumsResponse = mockk<TopAlbumsResponse>(relaxed = true)
            val albumResponse = mockk<AlbumResponse>(relaxed = true)
            val artistResponse = mockk<ArtistResponse>(relaxed = true)
            every { topalbums } returns topAlbumsResponse
            every { topAlbumsResponse.album } returns listOf(albumResponse)
            every { albumResponse.mbid } returns "mbid"
            every { albumResponse.name } returns "name"
            every { albumResponse.image } returns listOf(imageResponse)
            every { albumResponse.artist } returns artistResponse
            every { artistResponse.name } returns "author"
            every { artistResponse.mbid } returns "authorId"
        }

    private fun prepareMockSearchArtistResponse(): SearchResponse =
        mockk<SearchResponse>(relaxed = true).apply {
            val matches = mockk<ArtistMatchesResponse>()
            val artistmatches = mockk<ArtistMatchResponse>()
            val artist = mockk<ArtistResponse>()
            every { results } returns matches
            every { matches.artistmatches } returns artistmatches
            every { artistmatches.artist } returns listOf(artist)
            every { artist.name } returns "name"
            every { artist.mbid } returns "mbid"
            every { artist.image } returns listOf(imageResponse)
        }
}