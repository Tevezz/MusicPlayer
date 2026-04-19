package com.matheus.musicplayer.player.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.matheus.musicplayer.player.Cache
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @ApplicationContext context: Context,
    @Assisted private val route: Route.Player
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Player): PlayerViewModel
    }

    private val playerManager = PlayerManager(context)
    private val player = playerManager.getPlayer()

    private val _uiState = MutableStateFlow(
        PlayerState(
            song = Cache.song // temporary until navigation/repo is wired
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        observePlayer()
        play()
    }

    private fun observePlayer() {

        player.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(state: Int) {
                val duration = player.duration.takeIf { it > 0 } ?: 0L

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
                    it.copy(position = player.currentPosition)
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

    override fun onCleared() {
        playerManager.release()
    }
}