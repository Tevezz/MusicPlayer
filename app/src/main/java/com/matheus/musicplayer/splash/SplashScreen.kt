package com.matheus.musicplayer.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matheus.musicplayer.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val sizeDp = remember { Animatable(90f) }

    LaunchedEffect(Unit) {
        sizeDp.animateTo(
            targetValue = 200f,
            animationSpec = tween(durationMillis = 1200)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
        delay(300)
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.splash_screen_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha.value),
            contentScale = ContentScale.FillBounds
        )
        Image(
//            painter = painterResource(R.drawable.ic_splash_screen),
            painter = painterResource(R.drawable.ic_splash_screen),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(sizeDp.value.dp)
                .alpha(alpha.value)
        )
    }
}
