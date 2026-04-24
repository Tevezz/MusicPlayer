package com.matheus.musicplayer.data.repository

import com.matheus.musicplayer.data.datasource.local.SongDao
import com.matheus.musicplayer.data.datasource.remote.ITunesAPI
import com.matheus.musicplayer.data.model.local.SongEntity
import com.matheus.musicplayer.domain.model.Song
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SongRepositoryTest {

    private val iTunesAPI: ITunesAPI = mockk()
    private val songDao: SongDao = mockk()
    private lateinit var repository: SongRepositoryImpl

    @Before
    fun setup() {
        repository = SongRepositoryImpl(iTunesAPI, songDao)
    }

    private val songEntity = SongEntity(
        trackId = 1L,
        trackName = "Song One",
        artistName = "Artist One",
        artworkUrl = "https://example.com/art.jpg",
        previewUrl = "https://example.com/preview.mp3",
        collectionId = 10L,
        collectionName = "Collection One",
        trackTimeMillis = 180000L
    )

    private val song = Song(
        trackId = 1L,
        trackName = "Song One",
        artistName = "Artist One",
        collectionId = 10L,
        collectionName = "Collection One",
        artworkUrl100 = "https://example.com/art.jpg",
        previewUrl = "https://example.com/preview.mp3",
        trackTimeMillis = 180000L
    )

    @Test
    fun `GetSong - Maps Song from DAO`() = runBlocking {
        coEvery { songDao.getSong(1L) } returns songEntity
        val result = repository.getSong(1L)
        result.trackId shouldBe songEntity.trackId
        result.trackName shouldBe songEntity.trackName
        result.artistName shouldBe songEntity.artistName
        result.artworkUrl100 shouldBe songEntity.artworkUrl
        result.previewUrl shouldBe songEntity.previewUrl
        result.collectionId shouldBe songEntity.collectionId
        result.collectionName shouldBe songEntity.collectionName
        result.trackTimeMillis shouldBe songEntity.trackTimeMillis
        coVerify(exactly = 1) { songDao.getSong(1L) }
    }

    @Test
    fun `Save Recently Played - Upserts Mapped Entity`() = runBlocking {
        val entitySlot = slot<SongEntity>()
        coJustRun { songDao.upsert(capture(entitySlot)) }
        repository.saveRecentlyPlayed(song)
        val captured = entitySlot.captured
        captured.trackId shouldBe song.trackId
        captured.trackName shouldBe song.trackName
        captured.artistName shouldBe song.artistName
        captured.artworkUrl shouldBe song.artworkUrl100
        captured.previewUrl shouldBe song.previewUrl
        captured.collectionId shouldBe song.collectionId
        captured.collectionName shouldBe song.collectionName
        captured.trackTimeMillis shouldBe song.trackTimeMillis
        captured.lastPlayedAt shouldNotBe null
        coVerify(exactly = 1) { songDao.upsert(any()) }
    }

    @Test
    fun `Save Recently Played - Defaults for null Song Fields`() = runBlocking {
        val entitySlot = slot<SongEntity>()
        coJustRun { songDao.upsert(capture(entitySlot)) }
        val spareSong = Song(
            trackId = 99L,
            trackName = null,
            artistName = null,
            collectionId = null,
            collectionName = null,
            artworkUrl100 = null,
            previewUrl = null,
            trackTimeMillis = null
        )
        repository.saveRecentlyPlayed(spareSong)
        val captured = entitySlot.captured
        captured.trackId shouldBe 99L
        captured.trackName shouldBe ""
        captured.artistName shouldBe ""
        captured.artworkUrl shouldBe ""
        captured.collectionId shouldBe 0L
    }

    @Test
    fun `Search Songs - Returns a non-null Flow`() {
        val flow = repository.searchSongs("test query")
        flow shouldNotBe null
    }

    @Test
    fun `getRecentlyPlayed returns a non-null Flow`() {
        val flow = repository.getRecentlyPlayed()
        flow shouldNotBe null
    }

    @Test
    fun `Get Song Played Before - Maps Entity To Song`() = runBlocking {
        coEvery { songDao.getSongPlayedBefore(1L) } returns songEntity
        val result = repository.getSongPlayedBefore(1L)
        result shouldNotBe null
        result!!.trackId shouldBe songEntity.trackId
        result.trackName shouldBe songEntity.trackName
        result.artistName shouldBe songEntity.artistName
        result.artworkUrl100 shouldBe songEntity.artworkUrl
        result.previewUrl shouldBe songEntity.previewUrl
        result.collectionId shouldBe songEntity.collectionId
        result.collectionName shouldBe songEntity.collectionName
        result.trackTimeMillis shouldBe songEntity.trackTimeMillis
        coVerify(exactly = 1) { songDao.getSongPlayedBefore(1L) }
    }

    @Test
    fun `Get Song Played Before - Returns Null When No Song Found`() = runBlocking {
        coEvery { songDao.getSongPlayedBefore(1L) } returns null
        val result = repository.getSongPlayedBefore(1L)
        result shouldBe null
        coVerify(exactly = 1) { songDao.getSongPlayedBefore(1L) }
    }

    @Test
    fun `Get Song Played After - Maps Entity To Song`() = runBlocking {
        coEvery { songDao.getSongPlayedAfter(1L) } returns songEntity
        val result = repository.getSongPlayedAfter(1L)
        result shouldNotBe null
        result!!.trackId shouldBe songEntity.trackId
        result.trackName shouldBe songEntity.trackName
        result.artistName shouldBe songEntity.artistName
        result.artworkUrl100 shouldBe songEntity.artworkUrl
        result.previewUrl shouldBe songEntity.previewUrl
        result.collectionId shouldBe songEntity.collectionId
        result.collectionName shouldBe songEntity.collectionName
        result.trackTimeMillis shouldBe songEntity.trackTimeMillis
        coVerify(exactly = 1) { songDao.getSongPlayedAfter(1L) }
    }

    @Test
    fun `Get Song Played After - Returns Null When No Song Found`() = runBlocking {
        coEvery { songDao.getSongPlayedAfter(1L) } returns null
        val result = repository.getSongPlayedAfter(1L)
        result shouldBe null
        coVerify(exactly = 1) { songDao.getSongPlayedAfter(1L) }
    }
}