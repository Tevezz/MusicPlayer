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
import com.matheus.musicplayer.album.ui.AlbumScreen
import com.matheus.musicplayer.album.viewmodel.AlbumViewModel
import com.matheus.musicplayer.player.ui.PlayerScreen
import com.matheus.musicplayer.player.viewmodel.PlayerViewModel
import com.matheus.musicplayer.route.Route
import com.matheus.musicplayer.song.ui.SongListScreen
import com.matheus.musicplayer.splash.ui.SplashScreen
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

                val backStack = rememberNavBackStack(Route.Splash)

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() ?: finish() },
                    entryProvider = entryProvider {
                        entry<Route.Splash> {
                            SplashScreen(
                                onNavigateToMain = {
                                    backStack.clear()
                                    backStack.add(Route.SongList)
                                }
                            )
                        }
                        entry<Route.SongList> {
                            SongListScreen(
                                viewModel = hiltViewModel(),
                                onNavigateToPlayer = { backStack.add(Route.Player(it)) },
                                onNavigateToAlbum = { backStack.add(Route.Album(it)) }
                            )
                        }
                        entry<Route.Player> { key ->
                            PlayerScreen(
                                viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.Factory>(
                                    key = key.trackId.toString(),
                                    creationCallback = { factory ->
                                        factory.create(key)
                                    }
                                ),
                                onNavigateToAlbum = { backStack.add(Route.Album(it)) },
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                        entry<Route.Album> { key ->
                            AlbumScreen(
                                viewModel = hiltViewModel<AlbumViewModel, AlbumViewModel.Factory>(
                                    key = key.trackId.toString(),
                                    creationCallback = { factory ->
                                        factory.create(key)
                                    }
                                ),
                                onNavigateToPlayer = { backStack.add(Route.Player(it)) },
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }

        }
    }
}