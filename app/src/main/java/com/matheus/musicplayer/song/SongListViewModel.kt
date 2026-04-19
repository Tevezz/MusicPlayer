package com.matheus.musicplayer.song

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.data.model.SongResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val iTunesAPI: ITunesAPI
) : ViewModel() {

    var songs by mutableStateOf<List<SongResponseDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadSongs() {
        viewModelScope.launch {
            isLoading = true

            try {
                songs = iTunesAPI.searchSongs(
                    term = "top hits",
                    limit = 20,
                    offset = 0
                ).results
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isLoading = false
        }
    }

}