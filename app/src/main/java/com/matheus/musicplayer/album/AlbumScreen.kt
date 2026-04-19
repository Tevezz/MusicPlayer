package com.matheus.musicplayer.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.matheus.musicplayer.R
import com.matheus.musicplayer.player.ui.PlayerTopBar

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel,
    onNavigateBack: () -> Unit
) {

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            AlbumTopBar(
                title = "Some Album", // TODO
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

//            AsyncImage(
//                model = artwork,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(RoundedCornerShape(16.dp))
//            )

        }
    }
}