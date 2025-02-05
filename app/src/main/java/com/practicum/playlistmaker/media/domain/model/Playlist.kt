package com.practicum.playlistmaker.media.domain.model


data class Playlist(
    val id: Int?,
    val title: String,
    val description: String?,
    val fileUri: String?,
    val tracks: List<String>,
    val count: Int
)
