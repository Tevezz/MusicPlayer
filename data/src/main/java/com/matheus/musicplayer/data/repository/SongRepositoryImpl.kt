package com.matheus.musicplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.datasource.SongDao
import com.matheus.musicplayer.data.datasource.SongPagingSource
import com.matheus.musicplayer.data.mapper.toEntity
import com.matheus.musicplayer.data.mapper.toSong
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
            config = PagingConfig( // TODO Move to paging source as a factory or constants at least
                pageSize = 40,
                initialLoadSize = 40,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SongPagingSource(iTunesAPI, searchQuery)
            }
        ).flow
    }

    override fun getRecentlyPlayed(): Flow<PagingData<Song>> {
        return Pager(
            config = PagingConfig(
                pageSize = 40,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                songDao.getRecentlyPlayed()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toSong() }
        }
    }

    override suspend fun saveRecentlyPlayed(song: Song) {
        val entity = song.toEntity()
        songDao.upsert(entity)
    }

    override suspend fun getSong(trackId: Long): Song {
        return songDao.getSong(trackId).toSong()
    }

    override suspend fun getSongPlayedBefore(currentTrackId: Long): Song? {
        return songDao.getSongPlayedBefore(currentTrackId)?.toSong()
    }

    override suspend fun getSongPlayedAfter(currentTrackId: Long): Song? {
        return songDao.getSongPlayedAfter(currentTrackId)?.toSong()
    }
}