package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.remote.SongResponseDto
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
        trackId = trackId!!,
        trackName = trackName,
        artistName = artistName,
        collectionId = collectionId,
        collectionName = collectionName,
        artworkUrl100 = artworkUrl100,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis
    )
}