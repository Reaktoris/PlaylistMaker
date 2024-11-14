package com.practicum.playlistmaker.domain.interactor.searchHistory

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun saveTrack(track: Track) {
        searchHistoryRepository.saveTrack(track)
    }

    override fun getTrackList(): MutableList<Track> {
        return searchHistoryRepository.getTrackList()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}