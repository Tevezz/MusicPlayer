package com.matheus.musicplayer.player.viewmodel

sealed interface PlayerEvent {
    data class NavToAlbum(val trackId: Long) : PlayerEvent
}