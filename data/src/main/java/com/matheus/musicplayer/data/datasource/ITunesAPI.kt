package com.matheus.musicplayer.data.datasource

import com.matheus.musicplayer.data.model.ITunesSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI { // TODO Make internal

    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ITunesSearchResponseDto
}