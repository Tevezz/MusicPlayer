package com.matheus.musicplayer.data.datasource

import com.matheus.musicplayer.data.model.remote.ITunesLookupResponseDto
import com.matheus.musicplayer.data.model.remote.ITunesSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ITunesAPI {

    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ITunesSearchResponseDto

    @GET("lookup")
    suspend fun lookupAlbum(
        @Query("id") collectionId: Long,
        @Query("entity") entity: String = "song"
    ): ITunesLookupResponseDto
}