package com.matheus.musicplayer.player.ui.mini

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matheus.musicplayer.player.manager.PlaybackState

@Composable
fun MiniPlayer(
    state: PlaybackState,
    onPlayPauseClick: () -> Unit
) {

    val song = state.song.song ?: return

    Surface(
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.White.copy(alpha = 0.2f)),
        color = Color.Black,
        tonalElevation = 8.dp,
        shadowElevation = 4.dp
    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniPlayerSongArtwork(
                    artworkUrl = song.artworkUrl100.orEmpty(),
                    trackName = song.trackName.orEmpty(),
                    artistName = song.artistName.orEmpty(),
                    modifier = Modifier.weight(1f)
                )
                MiniPlayerControls(
                    isPlaying = state.controls.isPlaying,
                    onPlayPauseClick = onPlayPauseClick
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            MiniPlayerProgressSlider(
                position = state.position.position,
                duration = state.position.duration,
                onSeek = {},
                modifier = Modifier.height(6.dp)
            )
        }
    }
}