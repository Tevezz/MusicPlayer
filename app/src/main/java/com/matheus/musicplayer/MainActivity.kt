package com.matheus.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.matheus.musicplayer.player.Cache
import com.matheus.musicplayer.player.ui.PlayerScreen
import com.matheus.musicplayer.player.viewmodel.PlayerViewModel
import com.matheus.musicplayer.route.Route
import com.matheus.musicplayer.song.ui.SongListScreen
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
                    onBack = { backStack.removeLastOrNull() ?: finish() },
                    entryProvider = entryProvider {
                        entry<Route.SongList> {
                            SongListScreen(viewModel = hiltViewModel()) {
                                backStack.add(Route.Player(Cache.song!!.trackId))
                            }
                        }
                        entry<Route.Player> { key ->
                            PlayerScreen(
                                viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.Factory>(
                                    creationCallback = { factory ->
                                        factory.create(key)
                                    }
                                ),
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }

        }
    }
}