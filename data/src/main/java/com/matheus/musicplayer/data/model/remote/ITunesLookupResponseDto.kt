package com.matheus.musicplayer.data.model.remote

internal data class ITunesLookupResponseDto(
    val resultCount: Int,
    val results: List<LookupItemResponseDto>
)