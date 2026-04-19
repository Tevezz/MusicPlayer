package com.matheus.musicplayer.data.model.remote

internal data class ITunesSearchResponseDto(
    val resultCount: Int,
    val results: List<SongResponseDto>
)
