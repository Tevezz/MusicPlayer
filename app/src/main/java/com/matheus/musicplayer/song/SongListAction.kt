package com.matheus.musicplayer.song

sealed interface SongListAction {
    data class OnSearchQueryChange(val searchQuery: String) : SongListAction
}