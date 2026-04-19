package com.matheus.musicplayer.domain.repository

import com.matheus.musicplayer.domain.model.Song

interface SongRepository {
    suspend fun searchSongs(searchQuery: String): Result<List<Song>>
}