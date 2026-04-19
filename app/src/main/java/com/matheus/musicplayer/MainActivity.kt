package com.matheus.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.matheus.musicplayer.route.Route
import com.matheus.musicplayer.song.SongListScreen
import com.matheus.musicplayer.ui.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {

                    val backStack = rememberNavBackStack(Route.SongList)

                    NavDisplay(
                        backStack = backStack,
                        onBack = {
                            if (backStack.size > 1) { backStack.removeFirstOrNull() } else finish()
                        },
                        entryProvider = entryProvider {
                            entry<Route.SongList> {
                                SongListScreen()
                            }
                        }
                    )
                }

        }
    }
}