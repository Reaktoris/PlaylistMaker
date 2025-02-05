package com.practicum.playlistmaker.media.domain.model


data class Playlist(
    val id: Int?,
    var title: String,
    var description: String?,
    var fileUri: String?,
    val tracks: MutableList<Int>,
    var count: Int
)
