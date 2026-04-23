package com.matheus.musicplayer.data.repository

import com.matheus.musicplayer.data.datasource.remote.ITunesAPI
import com.matheus.musicplayer.data.mapper.toAlbum
import com.matheus.musicplayer.domain.model.Album
import com.matheus.musicplayer.domain.repository.AlbumRepository
import javax.inject.Inject

internal class AlbumRepositoryImpl @Inject constructor(
    private val iTunesAPI: ITunesAPI
) : AlbumRepository {

    override suspend fun getAlbum(collectionId: Long): Album {
        return iTunesAPI.lookupAlbum(
            collectionId = collectionId
        ).results.toAlbum()
    }

}