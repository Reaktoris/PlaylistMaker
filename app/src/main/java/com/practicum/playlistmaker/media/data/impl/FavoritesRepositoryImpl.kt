package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media.domain.FavoritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val appDatabase: AppDatabase) : FavoritesRepository {
    override suspend fun addFavoriteTrack(track: Track) {
        appDatabase.getTrackDao().insertTrack(convertFromTrack(track))
    }
    override suspend fun deleteFavoriteTrack(id: Int) {
        appDatabase.getTrackDao().deleteTrack(id)
    }
    override fun getFavoriteTracks(): Flow<List<Track>> = flow{
        val trackEntities = appDatabase.getTrackDao().getTracks()
        emit(convertFromTrackEntities(trackEntities))
    }.map{it.reversed()}
    override suspend fun isFavoriteCheck(id: Int): Boolean {
        return appDatabase.getTrackDao().getTracksID().contains(id)
    }


    private fun convertFromTrackEntities(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            Track(
                trackEntity.trackName,
                trackEntity.artistName,
                trackEntity.trackTimeMillis,
                trackEntity.artworkUrl100,
                trackEntity.previewUrl,
                trackEntity.trackId,
                trackEntity.collectionName,
                trackEntity.releaseDate,
                trackEntity.primaryGenreName,
                trackEntity.country,
                isFavorite = true)
        }
    }

    private fun convertFromTrack(track: Track): TrackEntity {
        return TrackEntity(
            id = null,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }
}