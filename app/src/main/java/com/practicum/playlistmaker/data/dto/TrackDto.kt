package com.practicum.playlistmaker.data.dto

data class TrackDto(val trackName: String,
                    val artistName: String,
                    val trackTimeMillis: Int,
                    val artworkUrl100: String,
                    val previewUrl: String,
                    val trackId: Int,
                    val collectionName: String,
                    val releaseDate: String,
                    val primaryGenreName: String,
                    val country: String)
