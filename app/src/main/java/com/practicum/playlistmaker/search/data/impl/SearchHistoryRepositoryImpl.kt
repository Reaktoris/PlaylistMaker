package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.data.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val searchHistory: SearchHistory) :
    SearchHistoryRepository {
    override fun saveTrack(track: Track) {
        searchHistory.saveTrack(
            TrackDto(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country)
        )
    }

    override fun getTrackList(): MutableList<Track>{
        val trackList = searchHistory.getTrackList()
        return (trackList.map { Track(
            it.trackName,
            it.artistName,
            it.trackTimeMillis,
            it.artworkUrl100,
            it.previewUrl,
            it.trackId,
            it.collectionName,
            it.releaseDate,
            it.primaryGenreName,
            it.country
        ) } as MutableList<Track>)
    }

    override fun clearHistory() {
        searchHistory.clearHistory()
    }
}