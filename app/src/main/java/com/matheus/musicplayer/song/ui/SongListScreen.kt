package com.matheus.musicplayer.song.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import com.matheus.musicplayer.song.viewmodel.SongListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val songs = state.songs.collectAsLazyPagingItems()

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
                searchQuery = state.searchQuery,
                onSearchQueryChanged = {
                    viewModel.onAction(SongListAction.OnSearchQueryChange(it))
                },
                onImeSearch = {},
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {

                when {
                    // Initial load
                    songs.loadState.refresh is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Error on first load
                    songs.loadState.refresh is LoadState.Error -> {
                        val error = songs.loadState.refresh as LoadState.Error

                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Something went wrong")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { songs.retry() }) {
                                Text("Retry")
                            }
                        }
                    }

                    // Empty state
                    songs.itemCount == 0 -> {
                        Text(
                            text = "No songs found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Content
                    else -> {
                        LazyColumn {
                            items(
                                count = songs.itemCount,
                                key = songs.itemKey { it.trackId }
                            ) { index ->
                                songs[index]?.let { song ->
                                    SongListItem(song) {
                                        viewModel.onAction(SongListAction.OnSongClick(it))
                                        onNavigateToPlayer()
                                    }
                                }
                            }

                            // Pagination loading
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

                            // Pagination error
                            if (songs.loadState.append is LoadState.Error) {
                                item {
                                    Button(
                                        onClick = { songs.retry() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}