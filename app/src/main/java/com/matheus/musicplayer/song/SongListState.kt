package com.matheus.musicplayer.song

import com.matheus.musicplayer.domain.model.Song

data class SongListState(
    val isLoading: Boolean = true,
    val songs: List<Song> = emptyList(),
    val searchQuery: String = ""
)
