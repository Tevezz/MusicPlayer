package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(trackId: Long): Result<Song> {
        return runCatching { repository.getSong(trackId) }
    }
}