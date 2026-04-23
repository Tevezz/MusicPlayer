package com.matheus.musicplayer.player.ui.fullscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matheus.musicplayer.R
import com.matheus.musicplayer.album.ui.AlbumBottomSheet
import com.matheus.musicplayer.player.viewmodel.PlayerEvent
import com.matheus.musicplayer.player.viewmodel.PlayerViewModel
import com.matheus.musicplayer.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onNavigateToAlbum: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.playbackState.collectAsStateWithLifecycle()

    val currentSong = state.song.song ?: return

    var isShowAlbumBottomSheet by remember { mutableStateOf(false) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PlayerEvent.NavToAlbum -> onNavigateToAlbum(event.trackId)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            PlayerTopBar(
                title = stringResource(R.string.now_playing),
                onNavigateBack = onNavigateBack,
                onOptionsClick = { isShowAlbumBottomSheet = true }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SongArtwork(
                artworkUrl = currentSong.getHigherResArtworkUrl().orEmpty(),
                trackName = currentSong.trackName.orEmpty(),
                artistName = currentSong.artistName.orEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            PlayerProgressSlider(
                position = state.position.position,
                duration = state.position.duration,
                onSeek = viewModel::seekTo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlayerControls(
                isPlaying = state.controls.isPlaying,
                isRepeating = state.controls.isRepeating,
                isNextEnabled = state.controls.hasNext,
                isPreviousEnabled = state.controls.hasPrevious,
                onPlayPauseClick = viewModel::onPlayPause,
                onNextClick = viewModel::onNext,
                onPreviousClick = viewModel::onPrevious,
                onRepeatClick = viewModel::onRepeatClick
            )
        }

        if (isShowAlbumBottomSheet) {
            AlbumBottomSheet(
                song = currentSong,
                onDismiss = { isShowAlbumBottomSheet = false },
                onViewAlbumClick = viewModel::onAlbumClick
            )
        }
    }
}