package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.remote.SongResponseDto
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SongResponseMapperTest {

    val dto1 = SongResponseDto(
        trackId = 123,
        trackName = "Song Name",
        artistName = "Artist Name",
        collectionId = 456,
        collectionName = "Collection Name",
        artworkUrl100 = "https://example.com/artwork.jpg",
        previewUrl = "https://example.com/preview.mp3",
        trackTimeMillis = 240000
    )

    val dto2 = SongResponseDto(
        trackId = 456,
        trackName = "Song Name 2",
        artistName = "Artist Name 2",
        collectionId = 999,
        collectionName = "Collection Name 2",
        artworkUrl100 = "https://example.com/artworkk.jpg",
        previewUrl = "https://example.com/previeww.mp3",
        trackTimeMillis = 250000
    )

    @Test
    fun `SongResponseDto to Song`() = runBlocking {
        val song = dto1.toSong()
        song.trackId shouldBe dto1.trackId
        song.trackName shouldBe dto1.trackName
        song.artistName shouldBe dto1.artistName
        song.collectionId shouldBe dto1.collectionId
        song.collectionName shouldBe dto1.collectionName
        song.artworkUrl100 shouldBe dto1.artworkUrl100
        song.previewUrl shouldBe dto1.previewUrl
        song.trackTimeMillis shouldBe dto1.trackTimeMillis
    }

    @Test
    fun `SongResponseDto list to Song list`() = runBlocking {
        val dtoList = listOf(dto1, dto2)
        val songList = dtoList.toSongList()
        songList.size shouldBe 2
        songList.first().trackId shouldBe dto1.trackId
        songList.last().trackName shouldBe dto2.trackName
    }

}