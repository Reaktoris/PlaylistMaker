package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface TrackInteractor {
    fun searchTracks(text: String): Flow<ConsumerData<List<Track>>>
}