package com.matheus.musicplayer.song

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.matheus.musicplayer.data.model.SongResponseDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel()
) {

    val songs = viewModel.songs
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.loadSongs()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                songs.isEmpty() -> {
                    Text(
                        text = "No songs found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn {
                        items(songs) { song ->
                            SongItem(song)
                        }
                    }
                }
            }

        }

    }

}

@Composable
fun SongItem(song: SongResponseDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        AsyncImage(
            model = song.artworkUrl100,
            contentDescription = song.trackName,
            onLoading = {
                println("Loading")
            },
            onSuccess = {
                println("Success")
            },
            onError = {
                println("Error loading: ${it}")
            },
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = song.trackName ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )

            Text(
                text = song.artistName ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}