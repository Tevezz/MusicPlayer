# MusicPlayer

A music player Android app built as a coding challenge. It searches songs, artists, and albums through the [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/Searching.html#//apple_ref/doc/uid/TP40017632-CH5-SW1), lets users play 30-second previews, and keeps a personal offline playlist of everything they've listened to.

---

## Screenshots

| ![image](https://github.com/Tevezz/MusicPlayer/blob/main/screenshots/splash-screen.png) | ![image](https://github.com/Tevezz/MusicPlayer/blob/main/screenshots/recently-played.png) | ![image](https://github.com/Tevezz/MusicPlayer/blob/main/screenshots/mini-player.png) | ![image](https://github.com/Tevezz/MusicPlayer/blob/main/screenshots/player.png) | ![image](https://github.com/Tevezz/MusicPlayer/blob/main/screenshots/album.png) |
| --- | --- | --- | --- | --- |

---

## Design Decisions

Think of it as a **search-and-play** music experience with an offline playlist built in the background.

You open the app, type a song or artist name, and get paginated results from iTunes. Tap a song and it starts playing immediately. Behind the scenes, that song is saved to a local Room database. Over time, the home screen fills up with your recently played tracks.

### Why only save songs the user plays

Saving every result from the iTunes API would flood the database with data the user will likely never touch. Instead, a song is written to Room the moment the user taps it, making that song part of their personal offline playlist. This mirrors how real music apps work: you search online, and save favorites locally. The recently played list on the home screen is this app's "offline playlist".

### The player iterates over local history, not the API

When the user taps next or previous in the player, the app queries Room for the song played just before or after the current one (ordered by `lastPlayedAt`). This keeps playback entirely offline once songs have been played, and it creates a natural "recently listened" queue without any extra state management.

### MediaSessionService for real background playback

Songs keep playing when you minimize the app. Using `MediaSessionService` instead of a plain foreground service means the app integrates with the Android media ecosystem: the system notification shows playback controls, Bluetooth headsets work out of the box, and the OS can manage the lifecycle safely. Every screen also shows a **mini player bar** at the bottom so you can always see what's playing and jump back to the full player in one tap.

### Two splash screens

Android 12+ enforces its own splash screen on every cold start. To satisfy both the challenge spec (a custom branded splash) and Android's requirements, the app uses `androidx.core.splashscreen` to handle the system splash, then transitions to a custom animated Compose splash before routing to the home screen.

---

## Tech Stack

| Category | Library / Technology                             |
|---|--------------------------------------------------|
| Language | Kotlin                                           |
| UI Toolkit | Jetpack Compose + Material 3                     |
| Architecture | MVVM + Clean Architecture                        |
| Dependency Injection | Hilt                                             |
| Navigation | Navigation3                                      |
| Network | Retrofit + GSON Converter                        |
| Local Database | Room                                             |
| Pagination | Android Paging 3                                 |
| Media Playback | ExoPlayer + MediaSessionService (Media3)         |
| Image Loading | Coil                                             |
| Splash Screen | AndroidX Core SplashScreen                       |
| Testing | JUnit 4, MockK, Kotest, Turbine, Coroutines Test |

---

## Running the App

1. Clone the repository
2. Open in Android Studio (Ladybug or newer recommended)
3. Run on a device or emulator with API 30+

No API keys or local configuration needed — the iTunes Search API is public.

> Pre-built APKs are available in the [Releases](../../releases) section if you'd prefer not to build from source.
