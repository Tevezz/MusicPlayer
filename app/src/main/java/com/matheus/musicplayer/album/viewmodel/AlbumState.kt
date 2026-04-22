package com.matheus.musicplayer.album.viewmodel

import com.matheus.musicplayer.domain.model.Album

data class AlbumState(
    val isLoading: Boolean = true,
    val album: Album? = null,
    val error: Int? = null
)
