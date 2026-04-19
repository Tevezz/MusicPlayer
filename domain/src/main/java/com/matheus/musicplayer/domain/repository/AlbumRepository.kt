package com.matheus.musicplayer.domain.repository

import com.matheus.musicplayer.domain.model.Album

interface AlbumRepository {
    suspend fun getAlbum(collectionId: Long): Album
}