package com.matheus.musicplayer.song

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.matheus.musicplayer.R
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.ui.theme.SongArtist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    viewModel: SongListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

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
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    state.songs.isEmpty() -> {
                        Text(
                            text = "No songs found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn {
                            items(state.songs) { song ->
                                SongItem(song)
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SongItem(song: Song) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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