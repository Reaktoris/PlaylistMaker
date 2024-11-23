package com.practicum.playlistmaker.domain.interactor.searchHistory

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun saveTrack(track: Track)

    fun getTrackList(): MutableList<Track>

    fun clearHistory()
}