package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface SearchHistoryInteractor {
    fun saveTrack(track: Track)

    fun getTrackList(): MutableList<Track>

    fun clearHistory()
}