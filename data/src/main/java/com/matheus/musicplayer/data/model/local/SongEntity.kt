package com.matheus.musicplayer.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
internal data class SongEntity(
    @PrimaryKey val trackId: Long,
    val trackName: String,
    val artistName: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val collectionId: Long,
    val collectionName: String?,
    val trackTimeMillis: Long?,
    val lastPlayedAt: Long? = null
)
