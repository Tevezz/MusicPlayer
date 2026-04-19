package com.matheus.musicplayer.data.model

internal data class ITunesLookupResponseDto(
    val resultCount: Int,
    val results: List<LookupItemResponseDto>
)