package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track

interface TrackInteractor {
    fun searchTracks(text: String, consumer: TrackConsumer<List<Track>?>)

    interface TrackConsumer<T> {
        fun consume(data: ConsumerData<T>)
    }
}