package com.matheus.musicplayer.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted private val route: Route.Player,
    private val playerManager: PlayerManager,
    private val getSongUseCase: GetSongUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Player): PlayerViewModel
    }

    private val _uiState = MutableStateFlow(PlayerState())
    val uiState = _uiState.asStateFlow()

    init {
        initialize()
        observePlayer()
    }

    private fun initialize() = viewModelScope.launch {
        val song = getSongUseCase(route.trackId).getOrNull()
        _uiState.update { it.copy(song = song) }
    }

    private fun observePlayer() {

        playerManager.getPlayer().addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(state: Int) {
                val duration = playerManager.getPlayer().duration.takeIf { it > 0 }
                    ?: PlayerState.DEFAULT_DURATION

                _uiState.update {
                    it.copy(
                        duration = duration
                    )
                }
            }
        })

        viewModelScope.launch {
            while (true) {
                _uiState.update {
                    it.copy(position = playerManager.getPlayer().currentPosition)
                }
                delay(100)
            }
        }
    }

    fun play() {
        val song = _uiState.value.song ?: return
        val url = song.previewUrl ?: return

        playerManager.play(url)
    }

    fun onPlayPause() {
        if (_uiState.value.isPlaying) {
            playerManager.pause()
        } else {
            play()
        }
    }

    fun seekTo(position: Long) {
        playerManager.seekTo(position)
    }

    fun onStopPlayback() {
        playerManager.reset()
    }
}