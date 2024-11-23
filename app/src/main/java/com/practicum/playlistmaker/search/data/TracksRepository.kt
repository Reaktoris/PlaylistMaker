package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.model.Track

interface TracksRepository {
    fun searchTracks(text: String): List<Track>?
}