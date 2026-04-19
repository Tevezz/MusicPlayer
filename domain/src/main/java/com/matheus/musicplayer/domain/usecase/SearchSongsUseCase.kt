package com.matheus.musicplayer.domain.usecase

import com.matheus.musicplayer.domain.repository.SongRepository
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(searchQuery: String) = repository.searchSongs(searchQuery)
}