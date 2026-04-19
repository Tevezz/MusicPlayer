package com.matheus.musicplayer.domain.usecase

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(
    private val repository: SongRepository
) {
    operator fun invoke(searchQuery: String): Flow<PagingData<Song>> {
        return repository.searchSongs(searchQuery)
    }
}