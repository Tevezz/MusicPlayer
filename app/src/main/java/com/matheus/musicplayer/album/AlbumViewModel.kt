package com.matheus.musicplayer.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.usecase.GetAlbumUseCase
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.route.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AlbumViewModel.Factory::class)
class AlbumViewModel @AssistedInject constructor(
    @Assisted private val route: Route.Album,
    private val getSongUseCase: GetSongUseCase,
    private val getAlbumUseCase: GetAlbumUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(route: Route.Album): AlbumViewModel
    }

    private val _state = MutableStateFlow(AlbumState())
    val state = _state.asStateFlow()

    init {
        getAlbum()
    }

    private fun getAlbum() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val song = getSongUseCase(route.trackId).getOrThrow()
        getAlbumUseCase(song.collectionId!!)
            .onSuccess { album ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        album = album
                    )
                }
            }

    }
}