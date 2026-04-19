package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Album
import com.matheus.musicplayer.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(collectionId: Long): Result<Album> {
        return runCatching { repository.getAlbum(collectionId) }
    }
}