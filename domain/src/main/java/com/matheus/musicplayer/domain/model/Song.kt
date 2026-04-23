package com.matheus.musicplayer.domain.model

data class Song(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionId: Long?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?
) {
    fun getHigherResArtworkUrl(size: Int = 600): String? =
        artworkUrl100?.replace("100x100", "${size}x${size}")
}
