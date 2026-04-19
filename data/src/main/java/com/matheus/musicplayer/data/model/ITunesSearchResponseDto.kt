package com.matheus.musicplayer.data.model

internal data class ITunesSearchResponseDto(
    val resultCount: Int,
    val results: List<SongResponseDto>
)
