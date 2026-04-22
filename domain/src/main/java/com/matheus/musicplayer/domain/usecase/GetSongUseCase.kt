package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.util.Response
import com.matheus.musicplayer.domain.util.runCatchingResponse
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(trackId: Long): Response<Song> {
        return runCatchingResponse { repository.getSong(trackId) }
    }
}