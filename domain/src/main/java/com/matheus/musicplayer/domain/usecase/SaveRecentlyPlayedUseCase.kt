package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.util.Response
import com.matheus.musicplayer.domain.util.runCatchingResponse
import javax.inject.Inject

class SaveRecentlyPlayedUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(song: Song): Response<Unit> {
        return runCatchingResponse { repository.saveRecentlyPlayed(song) }
    }
}