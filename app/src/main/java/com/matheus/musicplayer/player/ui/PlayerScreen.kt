package com.matheus.musicplayer.player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.matheus.musicplayer.R
import com.matheus.musicplayer.album.AlbumBottomSheet
import com.matheus.musicplayer.player.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onNavigateToAlbum: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val song = state.song ?: return

    val artwork = song.artworkUrl100?.replace("100x100", "600x600")
    var isShowAlbumBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            PlayerTopBar(
                title = stringResource(R.string.now_playing),
                onNavigateBack = onNavigateBack,
                onOptionsClick = {
                    isShowAlbumBottomSheet = true
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            AsyncImage(
                model = artwork,
                contentDescription = null,
                modifier = Modifier
                    .size(264.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = song.trackName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = song.artistName.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress Slider
            PlayerProgressSlider(
                position = state.position,
                duration = state.duration,
                onSeek = viewModel::seekTo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlayerControls(
                isPlaying = state.isPlaying,
                onPlayPauseClick = { viewModel.onPlayPause() }
            )
        }

        if (isShowAlbumBottomSheet) {
            AlbumBottomSheet(
                song = song,
                onDismiss = { isShowAlbumBottomSheet = false },
                onViewAlbumClick = { onNavigateToAlbum(song.trackId) } // TODO Needs to be an event
            )
        }
    }
}

