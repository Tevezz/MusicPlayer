package com.matheus.musicplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.datasource.SongDao
import com.matheus.musicplayer.data.datasource.SongPagingSource
import com.matheus.musicplayer.data.mapper.toEntity
import com.matheus.musicplayer.data.mapper.toSongList
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val iTunesAPI: ITunesAPI,
    private val songDao: SongDao
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

    override fun getRecentlyPlayed(): Flow<PagingData<Song>> {
        return songDao.getRecentlyPlayed()
            .map { list ->
                PagingData.from(list.toSongList())
            }
    }

    override suspend fun saveRecentlyPlayed(song: Song) {
        val entity = song.toEntity()
        songDao.upsert(entity)
    }
}