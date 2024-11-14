package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun saveTrack(track: Track)

    fun getTrackList(): MutableList<Track>

    fun clearHistory()
}