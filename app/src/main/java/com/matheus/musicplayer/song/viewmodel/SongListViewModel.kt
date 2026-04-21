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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SongListViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase,
    private val getRecentlyPlayedUseCase: GetRecentlyPlayedUseCase,
    private val saveRecentlyPlayedUseCase: SaveRecentlyPlayedUseCase
) : ViewModel() {

    private val _events = Channel<SongListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val songs: Flow<PagingData<Song>> =
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                when {
                    query.isBlank() -> getRecentlyPlayedUseCase()
                    else -> searchSongsUseCase(query)
                }
            }
            .cachedIn(viewModelScope)

    fun onSearchChange(searchQuery: String) = viewModelScope.launch {
        _searchQuery.value = searchQuery
    }

    fun onSongClick(song: Song) = viewModelScope.launch {
        saveRecentlyPlayedUseCase(song)
        _events.send(SongListEvent.NavToPlayer(song.trackId))
    }

    fun onAlbumClick(song: Song) = viewModelScope.launch {
        _events.send(SongListEvent.NavToAlbum(song.trackId))
    }
}