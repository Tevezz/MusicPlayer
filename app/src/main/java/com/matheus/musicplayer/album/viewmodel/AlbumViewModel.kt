package com.matheus.musicplayer.album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.R
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.usecase.GetAlbumUseCase
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.domain.usecase.SaveRecentlyPlayedUseCase
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AlbumViewModel.Factory::class)
class AlbumViewModel @AssistedInject constructor(
    @Assisted private val route: Route.Album,
    private val getSongUseCase: GetSongUseCase,
    private val getAlbumUseCase: GetAlbumUseCase,
    private val saveRecentlyPlayedUseCase: SaveRecentlyPlayedUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Album): AlbumViewModel
    }

    private val _events = Channel<AlbumEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(AlbumState())
    val state = _state.asStateFlow()

    init {
        getAlbum()
    }

    private fun getAlbum() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val song = getSongUseCase(route.trackId).resultOrNull()
        val collectionId = song?.collectionId

        // Safeguard, but should not happen.
        if (song == null || collectionId == null) {
            _state.update { it.copy(isLoading = false, error = R.string.song_not_found) }
            return@launch
        }

        getAlbumUseCase(collectionId)
            .onSuccess { album ->
                _state.update { it.copy(isLoading = false, album = album) }
            }
            .onError {
                _state.update { it.copy(isLoading = false, error = R.string.failed_to_load_album) }
            }
    }

    fun onSongClicked(song: Song) = viewModelScope.launch {
        saveRecentlyPlayedUseCase(song)
            .onSuspendSuccess {
                _events.send(AlbumEvent.NavToPlayer(song.trackId))
            }
    }
}