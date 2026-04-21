package com.matheus.musicplayer.album.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.matheus.musicplayer.album.viewmodel.AlbumEvent
import com.matheus.musicplayer.album.viewmodel.AlbumViewModel
import com.matheus.musicplayer.player.ui.PlayerTopBar
import com.matheus.musicplayer.song.ui.SongListItem
import com.matheus.musicplayer.util.ObserveAsEvents

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel,
    onNavigateToPlayer: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AlbumEvent.NavToPlayer -> onNavigateToPlayer(event.trackId)
        }
    }

    AnimatedContent(
        targetState = state,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
        label = "AlbumContent"
    ) { animatedState ->

        when {
            animatedState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            }

            animatedState.error != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = animatedState.error,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            animatedState.album != null -> {
                val album = animatedState.album
                Scaffold(
                    containerColor = Color.Black,
                    topBar = {
                        PlayerTopBar(
                            title = album.collectionName,
                            onNavigateBack = onNavigateBack
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = album.artworkUrl100,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = album.collectionName,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = album.artistName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            for (song in album.songs) {
                                SongListItem(
                                    song = song,
                                    showMoreIcon = false,
                                    onClick = viewModel::onSongClicked,
                                    onMoreClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
