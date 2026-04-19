package com.matheus.musicplayer.data.mapper

import com.matheus.musicplayer.data.model.remote.LookupItemResponseDto
import com.matheus.musicplayer.data.model.remote.LookupResponseType
import com.matheus.musicplayer.domain.model.Album
import com.matheus.musicplayer.domain.model.Song

internal fun List<LookupItemResponseDto>.toAlbum(): Album {
    val album = first { it.wrapperType == LookupResponseType.COLLECTION.name.lowercase() }
    val songs = mapNotNull {
        try {
            it.toSong()
        } catch (e: Exception) {
            null
        }
    }
    return Album(
        artistName = album.artistName!!,
        collectionId = album.collectionId!!,
        collectionName = album.collectionName!!,
        artworkUrl100 = album.artworkUrl100!!,
        songs = songs
    )
}

internal fun LookupItemResponseDto.toSong(): Song {
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