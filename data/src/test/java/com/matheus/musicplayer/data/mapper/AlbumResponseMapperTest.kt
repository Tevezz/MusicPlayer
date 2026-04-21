package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.remote.LookupItemResponseDto
import com.matheus.musicplayer.data.model.remote.LookupResponseType
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class AlbumResponseMapperTest {

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
    fun `LookupItemResponseDto list to Album`() = runBlocking {
        val album = listOf(collectionDto, trackDto1, trackDto2).toAlbum()
        album.collectionId shouldBe collectionDto.collectionId
        album.collectionName shouldBe collectionDto.collectionName
        album.artistName shouldBe collectionDto.artistName
        album.artworkUrl100 shouldBe collectionDto.artworkUrl100
    }

    @Test
    fun `Album songs exclude items with null trackId`() = runBlocking {
        val album = listOf(collectionDto, trackDto1, trackDto2).toAlbum()
        album.songs.size shouldBe 2
        val trackIds = album.songs.map { it.trackId }
        trackIds shouldBe listOf(trackDto1.trackId, trackDto2.trackId)
    }

    @Test
    fun `LookupItemResponseDto track to Song`() = runBlocking {
        val song = trackDto1.toSong()
        song.trackId shouldBe trackDto1.trackId
        song.trackName shouldBe trackDto1.trackName
        song.artistName shouldBe trackDto1.artistName
        song.collectionId shouldBe trackDto1.collectionId
        song.collectionName shouldBe trackDto1.collectionName
        song.artworkUrl100 shouldBe trackDto1.artworkUrl100
        song.previewUrl shouldBe trackDto1.previewUrl
        song.trackTimeMillis shouldBe trackDto1.trackTimeMillis
    }
}
