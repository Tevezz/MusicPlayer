package com.matheus.musicplayer.domain.repository

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun searchSongs(searchQuery: String): Flow<PagingData<Song>>
}