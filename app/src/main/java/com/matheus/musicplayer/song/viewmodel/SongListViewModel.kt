package com.matheus.musicplayer.song.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.usecase.GetRecentlyPlayedUseCase
import com.matheus.musicplayer.domain.usecase.SaveRecentlyPlayedUseCase
import com.matheus.musicplayer.domain.usecase.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase,
    private val getRecentlyPlayedUseCase: GetRecentlyPlayedUseCase,
    private val saveRecentlyPlayedUseCase: SaveRecentlyPlayedUseCase
) : ViewModel() {

    private val _events = Channel<SongListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(SongListState())
    val state = _state.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onAction(action: SongListAction) {
        when (action) {
            is SongListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.searchQuery) }
            }

            is SongListAction.OnSongClick -> {
                handleSongClick(action.song)
            }
        }
    }

    private fun observeSearchQuery() {
        val pagingFlow =
            _state
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(500)
                .flatMapLatest { query ->
                    when {
                        query.isBlank() -> {
                            getRecentlyPlayedUseCase()
                        }

                        query.length < 2 -> {
                            flowOf(PagingData.empty())
                        }

                        else -> {
                            searchSongsUseCase(query)
                        }
                    }
                }
                .cachedIn(viewModelScope)

        _state.update { it.copy(songs = pagingFlow) }
    }

    private fun handleSongClick(song: Song) = viewModelScope.launch {
        saveRecentlyPlayedUseCase(song)
        _events.send(SongListEvent.NavToPlayer(song.trackId))
    }
}