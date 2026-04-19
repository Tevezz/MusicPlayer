package com.matheus.musicplayer.song.viewmodel

sealed interface SongListEvent {
    data class NavToPlayer(val trackId: Long) : SongListEvent
}