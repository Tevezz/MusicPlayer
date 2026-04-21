package com.matheus.musicplayer.data

import com.matheus.musicplayer.data.mapper.toEntity
import com.matheus.musicplayer.data.mapper.toSong
import com.matheus.musicplayer.data.mapper.toSongList
import com.matheus.musicplayer.data.model.local.SongEntity
import com.matheus.musicplayer.domain.model.Song
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SongEntityMapperTest {

    val entity1 = SongEntity(
        trackId = 1L,
        trackName = "Song One",
        artistName = "Artist One",
        artworkUrl = "https://example.com/art1.jpg",
        previewUrl = "https://example.com/preview1.mp3",
        collectionId = 10L,
        collectionName = "Collection One",
        trackTimeMillis = 180000L
    )

    val entity2 = SongEntity(
        trackId = 2L,
        trackName = "Song Two",
        artistName = "Artist Two",
        artworkUrl = "https://example.com/art2.jpg",
        previewUrl = "https://example.com/preview2.mp3",
        collectionId = 20L,
        collectionName = "Collection Two",
        trackTimeMillis = 200000L
    )

    val song = Song(
        trackId = 3L,
        trackName = "Song Three",
        artistName = "Artist Three",
        collectionId = 30L,
        collectionName = "Collection Three",
        artworkUrl100 = "https://example.com/art3.jpg",
        previewUrl = "https://example.com/preview3.mp3",
        trackTimeMillis = 220000L
    )

    @Test
    fun `SongEntity to Song`() = runBlocking {
        val result = entity1.toSong()
        result.trackId shouldBe entity1.trackId
        result.trackName shouldBe entity1.trackName
        result.artistName shouldBe entity1.artistName
        result.artworkUrl100 shouldBe entity1.artworkUrl
        result.previewUrl shouldBe entity1.previewUrl
        result.collectionId shouldBe entity1.collectionId
        result.collectionName shouldBe entity1.collectionName
        result.trackTimeMillis shouldBe entity1.trackTimeMillis
    }

    @Test
    fun `SongEntity list to Song list`() = runBlocking {
        val result = listOf(entity1, entity2).toSongList()
        result.size shouldBe 2
        result.first().trackId shouldBe entity1.trackId
        result.last().trackName shouldBe entity2.trackName
    }

    @Test
    fun `Song to SongEntity`() = runBlocking {
        val result = song.toEntity()
        result.trackId shouldBe song.trackId
        result.trackName shouldBe song.trackName
        result.artistName shouldBe song.artistName
        result.artworkUrl shouldBe song.artworkUrl100
        result.previewUrl shouldBe song.previewUrl
        result.collectionId shouldBe song.collectionId
        result.collectionName shouldBe song.collectionName
        result.trackTimeMillis shouldBe song.trackTimeMillis
    }

    @Test
    fun `Song with null optional fields to SongEntity uses defaults`() = runBlocking {
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
        val result = spareSong.toEntity()
        result.trackId shouldBe 99L
        result.trackName shouldBe ""
        result.artistName shouldBe ""
        result.artworkUrl shouldBe ""
        result.collectionId shouldBe 0L
    }
}
