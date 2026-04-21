package com.matheus.musicplayer.data.repository

import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.model.remote.ITunesLookupResponseDto
import com.matheus.musicplayer.data.model.remote.LookupItemResponseDto
import com.matheus.musicplayer.data.model.remote.LookupResponseType
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class AlbumRepositoryTest {

    private val iTunesAPI: ITunesAPI = mockk()
    private lateinit var repository: AlbumRepositoryImpl

    @Before
    fun setup() {
        repository = AlbumRepositoryImpl(iTunesAPI)
    }

    private val collectionDto = LookupItemResponseDto(
        wrapperType = LookupResponseType.COLLECTION.name.lowercase(),
        collectionId = 100L,
        collectionName = "Album Name",
        artistName = "Artist Name",
        artworkUrl100 = "https://example.com/art.jpg",
        trackId = null,
        trackName = null,
        previewUrl = null,
        trackTimeMillis = null
    )

    private val trackDto1 = LookupItemResponseDto(
        wrapperType = LookupResponseType.TRACK.name.lowercase(),
        trackId = 1L,
        trackName = "Track One",
        artistName = "Artist Name",
        artworkUrl100 = "https://example.com/art.jpg",
        previewUrl = "https://example.com/preview1.mp3",
        trackTimeMillis = 180000L,
        collectionId = 100L,
        collectionName = "Album Name"
    )

    private val trackDto2 = LookupItemResponseDto(
        wrapperType = LookupResponseType.TRACK.name.lowercase(),
        trackId = 2L,
        trackName = "Track Two",
        artistName = "Artist Name",
        artworkUrl100 = "https://example.com/art.jpg",
        previewUrl = "https://example.com/preview2.mp3",
        trackTimeMillis = 200000L,
        collectionId = 100L,
        collectionName = "Album Name"
    )

    @Test
    fun `GetAlbum - Maps Album Correctly`() = runBlocking {
        coEvery { iTunesAPI.lookupAlbum(collectionId = 100L) } returns ITunesLookupResponseDto(
            resultCount = 3,
            results = listOf(collectionDto, trackDto1, trackDto2)
        )
        val album = repository.getAlbum(100L)
        album.collectionId shouldBe collectionDto.collectionId
        album.collectionName shouldBe collectionDto.collectionName
        album.artistName shouldBe collectionDto.artistName
        album.artworkUrl100 shouldBe collectionDto.artworkUrl100
        coVerify(exactly = 1) { iTunesAPI.lookupAlbum(collectionId = 100L) }
    }

    @Test
    fun `GetAlbum - Maps Songs from API`() = runBlocking {
        coEvery { iTunesAPI.lookupAlbum(collectionId = 100L) } returns ITunesLookupResponseDto(
            resultCount = 3,
            results = listOf(collectionDto, trackDto1, trackDto2)
        )
        val album = repository.getAlbum(100L)
        album.songs.size shouldBe 2
        album.songs[0].trackId shouldBe trackDto1.trackId
        album.songs[1].trackId shouldBe trackDto2.trackId
    }

    @Test
    fun `GetAlbum - Calls API`() = runBlocking {
        val targetId = 999L
        coEvery { iTunesAPI.lookupAlbum(collectionId = targetId) } returns ITunesLookupResponseDto(
            resultCount = 1,
            results = listOf(
                collectionDto.copy(
                    collectionId = targetId,
                    collectionName = "Other Album",
                    artistName = "Other Artist",
                    artworkUrl100 = "https://example.com/other.jpg"
                )
            )
        )
        repository.getAlbum(targetId)
        coVerify(exactly = 1) { iTunesAPI.lookupAlbum(collectionId = targetId) }
    }
}