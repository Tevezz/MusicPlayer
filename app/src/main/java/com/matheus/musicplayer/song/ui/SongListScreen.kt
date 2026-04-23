package com.matheus.musicplayer.song.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.matheus.musicplayer.R
import com.matheus.musicplayer.album.ui.AlbumBottomSheet
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.player.ui.mini.MiniPlayer
import com.matheus.musicplayer.song.viewmodel.SongListEvent
import com.matheus.musicplayer.song.viewmodel.SongListViewModel
import com.matheus.musicplayer.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    viewModel: SongListViewModel,
    onNavigateToPlayer: (Long) -> Unit,
    onNavigateToAlbum: (Long) -> Unit
) {
    val songs = viewModel.songs.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    var isSearching by remember(searchQuery) {
        mutableStateOf(searchQuery.isNotEmpty())
    }

    val isRefreshing = songs.loadState.refresh is LoadState.Loading
    val pullToRefreshState = rememberPullToRefreshState()
    var songToShowAlbumSheet by remember { mutableStateOf<Song?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SongListEvent.NavToPlayer -> onNavigateToPlayer(event.trackId)
            is SongListEvent.NavToAlbum -> onNavigateToAlbum(event.trackId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(playbackState.song.song != null) {
                MiniPlayer(
                    state = playbackState,
                    onPlayPauseClick = viewModel::onPlayPause
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.songs_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "SearchIcon"
                ) { searching ->
                    if (!searching) {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isSearching,
                enter = fadeIn(tween(300)) + expandVertically(animationSpec = tween(300)),
                exit = fadeOut(tween(300)) + shrinkVertically(animationSpec = tween(300))
            ) {
                SongSearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = viewModel::onSearchChange,
                    onImeSearch = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                )
            }

            if (!isSearching) {
                Spacer(modifier = Modifier.size(16.dp))
            }

            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = { songs.refresh() },
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (songs.loadState.refresh is LoadState.NotLoading && songs.itemCount == 0) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.search_for_a_song),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }

                    items(
                        count = songs.itemCount,
                        key = songs.itemKey { it.trackId }
                    ) { index ->
                        songs[index]?.let { song ->
                            SongListItem(
                                song = song,
                                showMoreIcon = true,
                                onClick = viewModel::onSongClick,
                                onMoreClick = { songToShowAlbumSheet = it },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }

                    if (songs.loadState.append is LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        songToShowAlbumSheet?.also { song ->
            AlbumBottomSheet(
                song = song,
                onDismiss = { songToShowAlbumSheet = null },
                onViewAlbumClick = viewModel::onAlbumClick
            )
        }
    }
}