package com.matheus.musicplayer.data.datasource.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.matheus.musicplayer.data.datasource.remote.ITunesAPI
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

            println("REQUEST → offset=$offset, limit=$limit")

            val response = api.searchSongs(
                term = query,
                limit = limit,
                offset = offset
            )

            val songs = response.results.toSongList()
            val prevKey = if (offset == 0) null else offset - limit
            // If the API returns fewer results than the limit, we've reached the end
            val nextKey = if (songs.isEmpty() || songs.size < limit) {
                null
            } else {
                offset + limit
            }

            LoadResult.Page(
                data = songs,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}