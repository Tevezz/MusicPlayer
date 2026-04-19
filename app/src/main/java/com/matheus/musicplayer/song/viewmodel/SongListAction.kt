package com.matheus.musicplayer.song.viewmodel

import com.matheus.musicplayer.domain.model.Song

sealed interface SongListAction {
    data class OnSongClick(val song: Song) : SongListAction
    data class OnSearchQueryChange(val searchQuery: String) : SongListAction
}