package com.matheus.musicplayer.data.model

data class SongResponseDto( // TODO Possible internal
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?
)
