package com.matheus.musicplayer.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.domain.usecase.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private var searchJob: Job? = null
    private val _state = MutableStateFlow(SongListState())
    val state = _state.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onAction(action: SongListAction) = viewModelScope.launch {
        when (action) {
            is SongListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.searchQuery) }
            }
        }
    }

    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        loadSongs()
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = loadSongs(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadSongs(term: String = "top hits") = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        try {
            val songs = searchSongsUseCase(term).getOrDefault(emptyList())
            _state.update { it.copy(songs = songs) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        _state.update { it.copy(isLoading = false) }
    }

}