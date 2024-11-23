package com.practicum.playlistmaker.domain.interactor.track

import com.practicum.playlistmaker.domain.model.ConsumerData
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val tracksRepository: TracksRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(text: String, consumer: TrackInteractor.TrackConsumer<List<Track>?>) {
        executor.execute {
            val trackList = tracksRepository.searchTracks(text)
            if(trackList == null){
                consumer.consume(ConsumerData.Error(""))
            } else{
                consumer.consume(ConsumerData.Data(tracksRepository.searchTracks(text)))
            }
        }
    }
}