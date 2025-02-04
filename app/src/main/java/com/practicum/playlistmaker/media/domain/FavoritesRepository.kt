package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(id: Int)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun isFavoriteCheck(id: Int): Boolean
}