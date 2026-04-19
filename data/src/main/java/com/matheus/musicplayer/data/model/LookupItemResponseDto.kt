package com.matheus.musicplayer.data.model

internal data class LookupItemResponseDto(
    val wrapperType: String?, // "collection" or "track"

    // Album fields
    val collectionId: Long?,
    val collectionName: String?,
    val artistName: String?,
    val artworkUrl100: String?,

    // Track fields
    val trackId: Long?,
    val trackName: String?,
    val previewUrl: String?,
    val trackTimeMillis: Long?
)