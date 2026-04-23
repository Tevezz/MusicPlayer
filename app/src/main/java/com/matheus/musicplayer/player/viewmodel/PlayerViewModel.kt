package com.matheus.musicplayer.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.usecase.GetSongPlayedAfterUseCase
import com.matheus.musicplayer.domain.usecase.GetSongPlayedBeforeUseCase
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted private val route: Route.Player,
    private val playerManager: PlayerManager,
    private val getSongUseCase: GetSongUseCase,
    private val getSongPlayedBeforeUseCase: GetSongPlayedBeforeUseCase,
    private val getSongPlayedAfterUseCase: GetSongPlayedAfterUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Player): PlayerViewModel
    }

    private val _events = Channel<PlayerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()

    private var olderSongId: Long? = null
    private var newerSongId: Long? = null

    init {
        loadSong(route.trackId)
        observePlayer()
    }

    private fun loadSong(trackId: Long) = viewModelScope.launch {
        loadSongData(trackId)
        play()
        loadAdjacentSongs(trackId)
    }

    private suspend fun loadSongData(trackId: Long) {
        _state.update {
            it.copy(
                song = SongState(getSongUseCase(trackId).resultOrNull()),
                controls = ControlsState(),
                position = PositionState()
            )
        }
    }

    private suspend fun loadAdjacentSongs(trackId: Long) {
        coroutineScope {
            val older = async { getSongPlayedBeforeUseCase(trackId) }
            val newer = async { getSongPlayedAfterUseCase(trackId) }
            olderSongId = older.await().resultOrNull()?.trackId
            newerSongId = newer.await().resultOrNull()?.trackId
        }
        _state.update {
            it.copy(
                controls = it.controls.copy(
                    canGoNext = olderSongId != null,
                    canGoPrevious = newerSongId != null
                )
            )
        }
    }

    private fun observePlayer() = viewModelScope.launch {
        val controller = playerManager.controllerFlow.filterNotNull().first()

        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(controls = it.controls.copy(isPlaying = isPlaying)) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                val duration = controller.duration.takeIf { it > 0 }
                    ?: PositionState.DEFAULT_DURATION
                _state.update { it.copy(position = it.position.copy(duration = duration)) }
                if (playbackState == Player.STATE_ENDED) onNextClick()
            }
        })

        while (true) {
            if (_state.value.controls.isPlaying) {
                _state.update { it.copy(position = it.position.copy(position = controller.currentPosition)) }
            }
            delay(100)
        }
    }

    private fun play() {
        val song = _state.value.song.song ?: return
        playerManager.play(song)
    }

    fun onPlayPause() {
        if (_state.value.controls.isPlaying) {
            playerManager.pause()
        } else {
            play()
        }
    }

    fun seekTo(position: Long) {
        playerManager.seekTo(position)
    }

    fun onRepeatClick() {
        val isRepeating = !_state.value.controls.isRepeating
        _state.update { it.copy(controls = it.controls.copy(isRepeating = isRepeating)) }
        playerManager.setRepeatMode(isRepeating)
    }

    fun onNextClick() {
        val id = olderSongId ?: return
        loadSong(id)
    }

    fun onPreviousClick() {
        val id = newerSongId ?: return
        loadSong(id)
    }

    fun onAlbumClick(song: Song) = viewModelScope.launch {
        _events.send(PlayerEvent.NavToAlbum(song.trackId))
    }
}
