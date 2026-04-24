package com.matheus.musicplayer.player.ui.fullscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matheus.musicplayer.R

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isRepeating: Boolean,
    isNextEnabled: Boolean,
    isPreviousEnabled: Boolean,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onRepeatClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier
                .size(72.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
        ) {
            AnimatedContent(
                targetState = isPlaying,
                transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(150)) },
                label = "PlayPause"
            ) { playing ->
                Icon(
                    imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playing) {
                        stringResource(R.string.pause)
                    } else stringResource(R.string.play),
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        IconButton(
            onClick = onPreviousClick,
            enabled = isPreviousEnabled,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_forward_bar),
                contentDescription = stringResource(R.string.previous),
                tint = if (isPreviousEnabled) Color.White else Color.White.copy(alpha = 0.35f),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        IconButton(
            onClick = onNextClick,
            enabled = isNextEnabled,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_forward_bar),
                contentDescription = stringResource(R.string.next),
                tint = if (isNextEnabled) Color.White else Color.White.copy(alpha = 0.35f),
                modifier = Modifier
                    .size(24.dp)
                    .rotate(180f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onRepeatClick,
                modifier = if (isRepeating) {
                    Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                } else {
                    Modifier
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_on_repeat),
                    contentDescription = stringResource(R.string.repeat),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
