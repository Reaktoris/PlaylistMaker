package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(text: String): Flow<List<Track>>
}