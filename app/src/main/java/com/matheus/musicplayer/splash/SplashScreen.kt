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

private const val INITIAL_ALPHA = 0f
private const val TARGET_ALPHA = 1f
private const val INITIAL_ICON_SIZE = 90f
private const val TARGET_ICON_SIZE = 200f
private const val ANIMATION_DURATION = 1200
private const val SPLASH_SCREEN_DELAY: Long = 300

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val alpha = remember { Animatable(INITIAL_ALPHA) }
    val sizeDp = remember { Animatable(INITIAL_ICON_SIZE) }

    LaunchedEffect(Unit) {
        sizeDp.animateTo(
            targetValue = TARGET_ICON_SIZE,
            animationSpec = tween(durationMillis = ANIMATION_DURATION)
        )
        alpha.animateTo(
            targetValue = TARGET_ALPHA,
            animationSpec = tween(durationMillis = ANIMATION_DURATION)
        )
        delay(SPLASH_SCREEN_DELAY)
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
            painter = painterResource(R.drawable.ic_splash_screen),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(sizeDp.value.dp)
                .alpha(alpha.value)
        )
    }
}
