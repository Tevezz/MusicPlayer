package com.matheus.musicplayer.song.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.ui.theme.SongArtist

@Composable
fun SongListItem(
    song: Song,
    onClick: (Song) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = { onClick(song) }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = song.artworkUrl100,
            contentDescription = song.trackName,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = song.trackName.orEmpty(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                maxLines = 1
            )

            Text(
                text = song.artistName.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = SongArtist,
                maxLines = 1
            )
        }
    }
}