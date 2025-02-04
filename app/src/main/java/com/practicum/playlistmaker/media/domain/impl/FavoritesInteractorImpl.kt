package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor{
    override suspend fun addFavoriteTrack(track: Track) {
        favoritesRepository.addFavoriteTrack(track)
    }
    override suspend fun deleteFavoriteTrack(id: Int) {
        favoritesRepository.deleteFavoriteTrack(id)
    }
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoriteTracks()
    }

    override suspend fun isFavoriteCheck(id: Int): Boolean {
        return favoritesRepository.isFavoriteCheck(id)
    }
}