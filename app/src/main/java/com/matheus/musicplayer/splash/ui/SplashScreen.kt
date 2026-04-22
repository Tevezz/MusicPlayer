package com.matheus.musicplayer.splash.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.matheus.musicplayer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val tealColor = remember { Animatable(Color.Black) }

    LaunchedEffect(Unit) {
        launch {
            tealColor.animateTo(
                targetValue = Color(0xFF0086A0),
                animationSpec = tween(durationMillis = 1000)
            )
        }
        delay(1500)
        onNavigateToMain()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()

        // Animation to make transition from Android 12 to custom Splash nicer
        val brush = Brush.linearGradient(
            colors = listOf(Color.Black, tealColor.value),
            start = Offset(0.3756f * w, 0.6971f * h),
            end = Offset(3.2657f * w, -0.0524f * h)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_splash_screen),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
    }
}
