package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrackEntity: PlaylistTrackEntity)
}