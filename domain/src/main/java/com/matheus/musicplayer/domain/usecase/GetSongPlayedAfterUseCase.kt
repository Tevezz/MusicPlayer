package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.util.Response
import com.matheus.musicplayer.domain.util.runCatchingResponse
import javax.inject.Inject

class GetSongPlayedAfterUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(currentTrackId: Long): Response<Song> {
        return runCatchingResponse {
            repository.getSongPlayedAfter(currentTrackId)
                ?: throw Exception("No song was found to play after the current track.")
        }
    }
}
