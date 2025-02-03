package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {
    fun saveTrack(track: Track)

    suspend fun getTrackList(): MutableList<Track>

    fun clearHistory()
}