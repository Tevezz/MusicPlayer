package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.SongResponseDto
import com.matheus.musicplayer.domain.model.Song

internal fun List<SongResponseDto>.toSongList(): List<Song> {
    return mapNotNull {
        try {
            it.toSong()
        } catch (e: Exception) {
            null
        }
    }
}

internal fun SongResponseDto.toSong(): Song {
    return Song(
        trackId = trackId ?: throw IllegalArgumentException(),
        trackName = trackName,
        artistName = artistName,
        collectionName = collectionName,
        artworkUrl100 = artworkUrl100,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis
    )
}