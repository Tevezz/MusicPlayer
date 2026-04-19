package com.matheus.musicplayer.data.repository

import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.mapper.toSongList
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val iTunesAPI: ITunesAPI
) : SongRepository {

    override suspend fun searchSongs(searchQuery: String): Result<List<Song>> {
        return runCatching {
            iTunesAPI.searchSongs(
                term = searchQuery,
                limit = 20,
                offset = 0
            ).results.toSongList()
        }
    }
}