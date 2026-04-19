package com.matheus.musicplayer.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.usecase.GetAlbumUseCase
import com.matheus.musicplayer.player.Cache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase
) : ViewModel() {

    val song = Cache.song

    init {
        getAlbum()
    }

    fun getAlbum() = viewModelScope.launch {
        val result = getAlbumUseCase(song?.collectionId!!)
        result.isSuccess
    }
}