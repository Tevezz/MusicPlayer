package com.matheus.musicplayer.domain.model

data class Album(
    val artistName: String,
    val collectionId: Long,
    val collectionName: String,
    val artworkUrl100: String?,
    val songs: List<Song>
)
