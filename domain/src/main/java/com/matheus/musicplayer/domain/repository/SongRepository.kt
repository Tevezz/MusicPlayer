package com.matheus.musicplayer.domain.repository

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun searchSongs(searchQuery: String): Flow<PagingData<Song>>
    fun getRecentlyPlayed(): Flow<PagingData<Song>>

    suspend fun saveRecentlyPlayed(song: Song)
    suspend fun getSong(trackId: Long): Song
    suspend fun getSongPlayedBefore(currentTrackId: Long): Song?
    suspend fun getSongPlayedAfter(currentTrackId: Long): Song?
}