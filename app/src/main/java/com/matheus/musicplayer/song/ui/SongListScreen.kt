package com.matheus.musicplayer.song.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.matheus.musicplayer.R
import com.matheus.musicplayer.song.viewmodel.SongListAction
import com.matheus.musicplayer.song.viewmodel.SongListEvent
import com.matheus.musicplayer.song.viewmodel.SongListViewModel
import com.matheus.musicplayer.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel(),
    onNavigateToPlayer: (Long) -> Unit
) {

    val songs = viewModel.songs.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SongListEvent.NavToPlayer -> onNavigateToPlayer(event.trackId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.songs_title),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            SongSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = {
                    viewModel.onAction(SongListAction.OnSearchQueryChange(it))
                },
                onImeSearch = {},
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {

                when {
                    songs.loadState.refresh is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    songs.loadState.refresh is LoadState.NotLoading && songs.itemCount == 0 -> {
                        Text(
                            text = stringResource(R.string.search_for_a_song),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn {
                            items(
                                count = songs.itemCount,
                                key = songs.itemKey { it.trackId }
                            ) { index ->
                                songs[index]?.let { song ->
                                    SongListItem(song) {
                                        viewModel.onAction(SongListAction.OnSongClick(it))
                                    }
                                }
                            }

                            if (songs.loadState.append is LoadState.Loading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}