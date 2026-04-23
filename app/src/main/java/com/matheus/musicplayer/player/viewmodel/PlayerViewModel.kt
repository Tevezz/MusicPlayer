package com.matheus.musicplayer.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted private val route: Route.Player,
    private val playerManager: PlayerManager
) : ViewModel(), PlayerManager by playerManager {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Player): PlayerViewModel
    }

    private val _events = Channel<PlayerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        playerManager.loadAndPlay(route.trackId)
    }

    fun onRepeatClick() {
        val isRepeating = !playbackState.value.controls.isRepeating
        playerManager.setRepeatMode(isRepeating)
    }

    fun onAlbumClick(song: Song) = viewModelScope.launch {
        _events.send(PlayerEvent.NavToAlbum(song.trackId))
    }
}
