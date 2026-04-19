package com.matheus.musicplayer.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.matheus.musicplayer.data.mapper.toSongList
import com.matheus.musicplayer.domain.model.Song

internal class SongPagingSource(
    private val api: ITunesAPI,
    private val query: String
) : PagingSource<Int, Song>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize

            val response = api.searchSongs(
                term = query,
                limit = limit,
                offset = offset
            )

            val songs = response.results.toSongList()

            LoadResult.Page(
                data = songs,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (songs.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(state.config.pageSize)
        }
    }
}