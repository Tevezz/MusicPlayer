package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.local.SongEntity
import com.matheus.musicplayer.domain.model.Song

internal fun List<SongEntity>.toSongList(): List<Song> {
    return mapNotNull {
        try {
            it.toSong()
        } catch (e: Exception) {
            null
        }
    }
}

internal fun SongEntity.toSong(): Song {
    return Song(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        collectionId = collectionId,
        collectionName = collectionName,
        artworkUrl100 = artworkUrl,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis
    )
}

internal fun Song.toEntity(): SongEntity {
    return SongEntity(
        trackId = trackId,
        trackName = trackName.orEmpty(),
        artistName = artistName.orEmpty(),
        artworkUrl = artworkUrl100.orEmpty(),
        previewUrl = previewUrl,
        collectionId = collectionId ?: 0, // TODO Needs to be not null
        collectionName = collectionName,
        trackTimeMillis = trackTimeMillis,
        lastPlayedAt = System.currentTimeMillis()
    )
}