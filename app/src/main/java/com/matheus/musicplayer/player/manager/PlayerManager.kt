package com.matheus.musicplayer.player.manager

import kotlinx.coroutines.flow.StateFlow

interface PlayerManager {
    val playbackState: StateFlow<PlaybackState>
    fun loadAndPlay(trackId: Long)
    fun onPlayPause()
    fun onNext()
    fun onPrevious()
    fun seekTo(position: Long)
    fun setRepeatMode(enabled: Boolean)
}