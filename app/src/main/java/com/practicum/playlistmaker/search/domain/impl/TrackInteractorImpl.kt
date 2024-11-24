package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TrackInteractor
import java.util.concurrent.Executors

class TrackInteractorImpl(private val tracksRepository: TracksRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(text: String, consumer: TrackInteractor.TrackConsumer<List<Track>?>) {
        executor.execute {
            val trackList = tracksRepository.searchTracks(text)
            if(trackList == null){
                consumer.consume(ConsumerData.Error())
            } else{
                consumer.consume(ConsumerData.Data(tracksRepository.searchTracks(text)))
            }
        }
    }
}