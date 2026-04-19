package com.matheus.musicplayer.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.matheus.musicplayer.domain.usecase.SearchSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

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
        }
    }

    private fun observeSearchQuery() {
        val pagingFlow =
            _state
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(500)
                .flatMapLatest { query ->

                    val effectiveQuery = when {
                        query.isBlank() -> "top hits"
                        query.length < 2 -> null
                        else -> query
                    }

                    effectiveQuery?.let {
                        searchSongsUseCase(it)
                    } ?: flowOf(PagingData.empty())
                }
                .cachedIn(viewModelScope)

        _state.update { it.copy(songs = pagingFlow) }
    }
}