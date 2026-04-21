package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Album
import com.matheus.musicplayer.domain.repository.AlbumRepository
import com.matheus.musicplayer.domain.util.Response
import com.matheus.musicplayer.domain.util.runCatchingResponse
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(collectionId: Long): Response<Album> {
        return runCatchingResponse { repository.getAlbum(collectionId) }
    }
}