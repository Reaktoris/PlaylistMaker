package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun saveTrack(track: Track) {
        searchHistoryRepository.saveTrack(track)
    }

    override suspend fun getTrackList(): MutableList<Track> {
        return searchHistoryRepository.getTrackList()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}