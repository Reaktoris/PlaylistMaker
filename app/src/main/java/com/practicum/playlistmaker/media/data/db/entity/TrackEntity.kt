package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val previewUrl: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)
