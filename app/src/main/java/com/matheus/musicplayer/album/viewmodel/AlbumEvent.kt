package com.matheus.musicplayer.album.viewmodel

sealed interface AlbumEvent {
    data class NavToPlayer(val trackId: Long) : AlbumEvent
}