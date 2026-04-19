package com.matheus.musicplayer.data.model

data class ITunesSearchResponseDto( // TODO possibly internal
    val resultCount: Int,
    val results: List<SongResponseDto>
)
