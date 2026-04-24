# MusicPlayer

A music player Android app built as a coding challenge. It searches songs, artists, and albums through the **Apple iTunes API**, lets users play 30-second previews, and keeps a personal offline playlist of everything they've listened to.

---

## What this app is

Think of it as a **search-and-play** music experience with an offline playlist built in the background.

You open the app, type a song or artist name, and get paginated results from iTunes. Tap a song — it starts playing immediately. Behind the scenes, that song is saved to a local Room database. Over time, the home screen fills up with your recently played tracks, which you can keep listening to even without an internet connection.

The player keeps running when you minimize the app. Thanks to **Android's `MediaSessionService`**, the song continues in the background and shows up in the system notification tray — just like Spotify or YouTube Music. Every screen shows a **mini player bar** at the bottom so you can always see what's playing and jump back to the full player in one tap.

The offline storage is intentional: instead of syncing the entire iTunes catalog to the database (which would be gigabytes of data you'll never use), only the songs you actually play get saved. It's the same philosophy real music apps use — you search online, and you have a local "listened" folder that works offline.

---

## Screenshots

> Screenshots will be added here.

---

## Tech Stack

| Category | Library / Technology | Version |
|---|---|---|
| Language | Kotlin | 2.3.20 |
| UI Toolkit | Jetpack Compose + Material 3 | BOM 2026.03.01 |
| Architecture | Hilt, ViewModel, StateFlow | — |
| Navigation | Navigation3 | 1.1.0 |
| Network | Retrofit + GSON Converter | 3.0.0 |
| Local Database | Room | 2.8.4 |
| Pagination | Android Paging 3 | 3.4.2 |
| Media Playback | Media3 / ExoPlayer | 1.3.1 |
| Image Loading | Coil | 3.4.0 |
| Splash Screen | AndroidX Core SplashScreen | 1.2.0 |
| Dependency Injection | Hilt | 2.59.2 |
| Testing | JUnit 4, MockK, Kotest, Turbine, Coroutines Test | — |

---

## Architecture

Three-module Clean Architecture:

```
┌──────────────────────────────────────────┐
│               :app  (Presentation)        │
│  Compose screens · ViewModels · Navigation│
│  PlayerManager · PlaybackService (Media3) │
└────────────────────┬─────────────────────┘
                     │ depends on
┌────────────────────▼─────────────────────┐
│              :domain  (Pure Kotlin)       │
│  Models · Repository interfaces          │
│  Use cases · Response<T> result type     │
└────────────────────┬─────────────────────┘
                     │ depends on
┌────────────────────▼─────────────────────┐
│               :data  (Android)            │
│  Retrofit (iTunes API) · Room DB         │
│  Paging source · Mappers · DI modules    │
└──────────────────────────────────────────┘
```

### Key patterns

- **MVVM** — ViewModels expose `StateFlow<UiState>` to Compose screens.
- **MVI-style events** — one-shot navigation actions use a `Channel<T>` collected as a `Flow` in the UI, preventing re-delivery on recomposition.
- **Repository pattern** — interfaces live in `:domain`; implementations live in `:data`. Swapping the network source doesn't touch the UI or domain layers.
- **Use cases** — each operation (search, get album, save/get recently played, get next/previous song) is a single-responsibility class injected into ViewModels.
- **Response\<T\>** — a custom sealed result type with chainable `.onSuccess()`, `.onError()`, and `.andThen()` operators used throughout the domain and data layers.

---

## Challenge Requirements

| Requirement | How it's met |
|---|---|
| **Kotlin** | 100% Kotlin (2.3.20), including Kotlin Serialization for navigation routes |
| **MVVM architecture** | `SongListViewModel`, `PlayerViewModel`, `AlbumViewModel` all expose `StateFlow` for state and `Channel`-backed flows for navigation events |
| **Tests** | 16 test files across all three modules — domain use cases, data mappers and repositories, and app-layer ViewModels. Uses JUnit4 · MockK · Kotest · Turbine |
| **API results pagination** | `SongPagingSource` (Paging 3) requests 40 items per page with offset-based pagination from the iTunes `/search` endpoint |
| **Kotlin Coroutines** | Used throughout: `viewModelScope`, `Flow`, `StateFlow`, `async/await` for parallel operations in `PlayerManagerImpl` |
| **Jetpack Compose** | The entire UI is Compose — all screens, the mini player, the splash animation, and all custom components |
| **Network abstraction** | `SongRepository` and `AlbumRepository` interfaces are defined in `:domain` with no Android or Retrofit imports. The Retrofit implementations in `:data` are bound via Hilt and can be replaced without changing any other layer |
| **Cache / offline-first** | Played songs are persisted in Room (`SongEntity`). The home screen shows recently played tracks from the local database. The player can navigate forward and backward through locally saved history even without a connection |

### Extra points

| Extra | Status |
|---|---|
| Error / state handling | Custom `Response<T>` sealed class propagates errors from network to ViewModel; loading and error states are represented in UI state |
| Forward / Backward on the player | `GetSongPlayedAfterUseCase` and `GetSongPlayedBeforeUseCase` query Room for the next and previous songs in play history |
| Song timeline | Full-screen player and mini player both show a progress slider. `PositionState` is updated on a 100 ms tick while playing |
| Background playback | `PlaybackService` (`MediaSessionService`) keeps ExoPlayer alive in the background and exposes a system media notification |
| Mini player | Persistent mini player composable shown on every screen except the full player and splash, with play/pause control and current song info |

---

## Design Decisions

### Why only save songs the user plays

Saving every result from the iTunes API would flood the database with data the user will likely never touch. Instead, a song is written to Room the moment the user taps it — making that song part of their personal offline playlist. This mirrors how real music apps work: you discover online, you own locally. The recently played list on the home screen is a direct window into that local playlist.

### The player iterates over local history, not the API

When the user taps next or previous in the player, the app queries Room for the song played just before or after the current one (ordered by `lastPlayedAt`). This keeps playback entirely offline once songs have been played, and it creates a natural "recently listened" queue without any extra state management.

### MediaSessionService for real background playback

Using `MediaSessionService` instead of a plain foreground service means the app integrates with the Android media ecosystem: the system notification shows playback controls, Bluetooth headsets work out of the box, and the OS can manage the lifecycle safely. `PlayerManagerImpl` communicates with the service through a `MediaController` connected via `SessionToken`.

### Two splash screens

Android 12+ enforces its own splash screen on every cold start. To satisfy both the challenge spec (a custom branded splash) and Android's requirements, the app uses `androidx.core.splashscreen` to handle the system splash, then transitions to a custom animated Compose splash before routing to the home screen.

---

## Running the App

1. Clone the repository
2. Open in Android Studio (Ladybug or newer recommended)
3. Run on a device or emulator with API 30+

No API keys or local configuration needed — the iTunes Search API is public.

> Pre-built APKs are available in the [Releases](../../releases) section if you'd prefer not to build from source.
