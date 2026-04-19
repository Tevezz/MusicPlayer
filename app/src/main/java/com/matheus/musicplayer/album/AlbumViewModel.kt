package com.matheus.musicplayer.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.usecase.GetAlbumUseCase
import com.matheus.musicplayer.player.Cache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumState())
    val state = _state.asStateFlow()

    init {
        getAlbum()
    }

    private fun getAlbum() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        getAlbumUseCase(Cache.song?.collectionId!!)
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