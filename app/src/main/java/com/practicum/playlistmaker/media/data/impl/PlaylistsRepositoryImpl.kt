package com.practicum.playlistmaker.media.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDatabase: AppDatabase) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow{
        emit(convertFromPlaylistEntities(appDatabase.getPlaylistDao().getPlaylists()))
    }.map{it.reversed()}

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val updatedPlaylist = playlist.copy(
            tracks = playlist.tracks.plus(track.trackId.toString()),
            count = playlist.count.plus(1)
        )
        appDatabase.getPlaylistDao().updatePlaylist(convertFromPlaylist(updatedPlaylist))
        appDatabase.getPlaylistTrackDao().insertPlaylistTrack(convertFromTrack(track))
    }

    private fun convertFromPlaylistEntities(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            Playlist(
                it.id,
                it.title,
                it.description,
                it.fileUri,
                Gson().fromJson(it.tracks, object : TypeToken<MutableList<String>>() {}.type),
                it.count
            )
        }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.fileUri,
            Gson().toJson(playlist.tracks),
            playlist.count
        )
    }

    private fun convertFromTrack(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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