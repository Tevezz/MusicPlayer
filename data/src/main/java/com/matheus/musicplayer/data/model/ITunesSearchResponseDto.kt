package com.matheus.musicplayer.data.model

data class ITunesSearchResponseDto(
    val resultCount: Int,
    val results: List<SongResponseDto>
)
