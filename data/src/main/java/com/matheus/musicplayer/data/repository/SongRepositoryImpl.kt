package com.matheus.musicplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.datasource.SongPagingSource
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val iTunesAPI: ITunesAPI
) : SongRepository {

    override fun searchSongs(searchQuery: String): Flow<PagingData<Song>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SongPagingSource(iTunesAPI, searchQuery)
            }
        ).flow
    }
}