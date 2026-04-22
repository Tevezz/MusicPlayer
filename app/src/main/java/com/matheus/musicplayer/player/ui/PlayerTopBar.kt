package com.matheus.musicplayer.player.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.matheus.musicplayer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopBar(
    title: String,
    onNavigateBack: () -> Unit,
    onOptionsClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        actions = {
            if (onOptionsClick != null) {
                IconButton(onClick = onOptionsClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options),
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}