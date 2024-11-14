package com.practicum.playlistmaker.domain.interactor.track

import com.practicum.playlistmaker.domain.model.ConsumerData
import com.practicum.playlistmaker.domain.model.Track

interface TrackInteractor {
    fun searchTracks(text: String, consumer: TrackConsumer<List<Track>?>)

    interface TrackConsumer<T> {
        fun consume(data: ConsumerData<T>)
    }
}