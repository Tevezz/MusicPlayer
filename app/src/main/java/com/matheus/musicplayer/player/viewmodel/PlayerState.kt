package com.matheus.musicplayer.player.viewmodel

import com.matheus.musicplayer.domain.model.Song

data class PlayerState(
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val isRepeating: Boolean = false,
    val position: Long = 0L,
    val duration: Long = DEFAULT_DURATION
) {
    companion object {
        const val DEFAULT_DURATION: Long = 30000L
    }
}
