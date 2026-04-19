package com.matheus.musicplayer.data.model

internal data class SongResponseDto(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val collectionId: Long?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?
)
