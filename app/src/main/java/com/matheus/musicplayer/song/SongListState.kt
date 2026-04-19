package com.matheus.musicplayer.song

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SongListState(
    val songs: Flow<PagingData<Song>> = flowOf(PagingData.empty()),
    val searchQuery: String = ""
)
