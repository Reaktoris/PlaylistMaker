package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {
    fun saveTrack(track: Track)

    fun getTrackList(): MutableList<Track>

    fun clearHistory()
}