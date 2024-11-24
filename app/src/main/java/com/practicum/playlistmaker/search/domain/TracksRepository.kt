package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface TracksRepository {
    fun searchTracks(text: String): List<Track>?
}