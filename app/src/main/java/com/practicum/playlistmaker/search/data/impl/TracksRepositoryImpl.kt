package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.TracksRepository
import com.practicum.playlistmaker.search.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(text: String): List<Track>? {
        val response = networkClient.doRequest(TrackRequest(text))
        return when (response.resultCode) {
            200 -> (response as TrackSearchResponse).results.map {
                Track(
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
                )
            }
            404 -> emptyList()
            else -> null
        }
    }
}