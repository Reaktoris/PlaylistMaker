package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun getPlaylistById(id: Int): Playlist
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track)
    suspend fun removePlaylistTrack(trackId: Int)
    fun getTracksFromPlaylist(tracks: List<Int>): Flow<List<Track>>

    suspend fun updatePlaylist(playlist: Playlist)

}