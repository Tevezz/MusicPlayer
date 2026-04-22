package com.matheus.musicplayer.main

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
import com.matheus.musicplayer.splash.SplashScreen
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

                        // Splash Screen
                        entry<Route.Splash> {
                            SplashScreen(
                                onNavigateToMain = {
                                    backStack.clear()
                                    backStack.add(Route.SongList)
                                }
                            )
                        }

                        // Song List Screen
                        entry<Route.SongList> {
                            SongListScreen(
                                viewModel = hiltViewModel(),
                                onNavigateToPlayer = {
                                    backStack.removeAll { route -> route is Route.Player }
                                    backStack.add(Route.Player(it))
                                },
                                onNavigateToAlbum = {
                                    backStack.removeAll { route -> route is Route.Album }
                                    backStack.add(Route.Album(it))
                                }
                            )
                        }

                        // Player Screen
                        entry<Route.Player> { key ->
                            PlayerScreen(
                                viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.Factory>(
                                    key = key.trackId.toString(),
                                    creationCallback = { factory ->
                                        factory.create(key)
                                    }
                                ),
                                onNavigateToAlbum = {
                                    backStack.removeAll { route -> route is Route.Album }
                                    backStack.add(Route.Album(it))
                                },
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }

                        // Album Screen
                        entry<Route.Album> { key ->
                            AlbumScreen(
                                viewModel = hiltViewModel<AlbumViewModel, AlbumViewModel.Factory>(
                                    key = key.trackId.toString(),
                                    creationCallback = { factory ->
                                        factory.create(key)
                                    }
                                ),
                                onNavigateToPlayer = {
                                    backStack.removeAll { route ->
                                        route is Route.Album || route is Route.Player
                                    }
                                    backStack.add(Route.Player(it))
                                },
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }

        }
    }
}