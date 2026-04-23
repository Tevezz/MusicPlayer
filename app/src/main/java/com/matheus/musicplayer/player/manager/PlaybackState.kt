package com.matheus.musicplayer.player.manager

import androidx.compose.runtime.Immutable
import com.matheus.musicplayer.domain.model.Song

@Immutable
data class PlaybackState(
    val song: SongState = SongState(),
    val controls: ControlsState = ControlsState(),
    val position: PositionState = PositionState()
)

@Immutable
data class SongState(val song: Song? = null)

data class ControlsState(
    val isPlaying: Boolean = false,
    val isRepeating: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false
)

data class PositionState(
    val position: Long = 0L,
    val duration: Long = DEFAULT_DURATION
) {
    companion object {
        const val DEFAULT_DURATION: Long = 30000L
    }
}