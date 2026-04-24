package com.matheus.musicplayer.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.player.manager.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel(), PlayerManager by playerManager {

    private val _events = Channel<PlayerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onRepeatClick() {
        val isRepeating = !playbackState.value.controls.isRepeating
        playerManager.setRepeatMode(isRepeating)
    }

    fun onAlbumClick(song: Song) = viewModelScope.launch {
        _events.send(PlayerEvent.NavToAlbum(song.trackId))
    }
}
