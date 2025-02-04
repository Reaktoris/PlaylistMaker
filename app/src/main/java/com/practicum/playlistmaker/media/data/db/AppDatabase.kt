package com.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmaker.media.data.db.dao.TrackDao
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getPlaylistTrackDao(): PlaylistTrackDao
}