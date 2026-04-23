package com.matheus.musicplayer.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matheus.musicplayer.util.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProgressSlider(
    position: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val safeDuration = duration.takeIf { it > 0 } ?: 1L

    Column(modifier = modifier) {

        Slider(
            value = position.toFloat(),
            onValueChange = { onSeek(it.toLong()) },
            valueRange = 0f..safeDuration.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),

            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.25f),
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.White, CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(100)),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                    ),
                    thumbTrackGapSize = 0.dp,
                    drawStopIndicator = null
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = position.formatTime(),
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                color = Color.White.copy(alpha = 0.6f)
            )
            Text(
                text = "-${(duration - position).coerceAtLeast(0).formatTime()}",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}