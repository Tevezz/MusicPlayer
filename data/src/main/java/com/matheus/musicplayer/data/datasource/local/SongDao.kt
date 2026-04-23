package com.matheus.musicplayer.data.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matheus.musicplayer.data.model.local.SongEntity

@Dao
internal interface SongDao {

    @Query("SELECT * FROM songs WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC")
    fun getRecentlyPlayed(): PagingSource<Int, SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: SongEntity)

    @Query("SELECT * FROM songs WHERE trackId = :id")
    suspend fun getSong(id: Long): SongEntity

    @Query("""
        SELECT * FROM songs
        WHERE lastPlayedAt IS NOT NULL
          AND lastPlayedAt < (SELECT lastPlayedAt FROM songs WHERE trackId = :currentTrackId)
        ORDER BY lastPlayedAt DESC
        LIMIT 1
    """)
    suspend fun getSongPlayedBefore(currentTrackId: Long): SongEntity?

    @Query("""
        SELECT * FROM songs
        WHERE lastPlayedAt IS NOT NULL
          AND lastPlayedAt > (SELECT lastPlayedAt FROM songs WHERE trackId = :currentTrackId)
        ORDER BY lastPlayedAt ASC
        LIMIT 1
    """)
    suspend fun getSongPlayedAfter(currentTrackId: Long): SongEntity?
}