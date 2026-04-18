package com.matheus.musicplayer.data.model

data class SongResponseDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?
)
