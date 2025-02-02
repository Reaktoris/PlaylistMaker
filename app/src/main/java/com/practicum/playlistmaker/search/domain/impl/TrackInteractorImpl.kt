package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TrackInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val tracksRepository: TracksRepository) : TrackInteractor {
    override fun searchTracks(text: String): Flow<ConsumerData<List<Track>>> {
        return tracksRepository.searchTracks(text).map {result ->
            if(result == null){
                ConsumerData.Error()
            } else{
                ConsumerData.Data(result)
            }
        }
        }
}