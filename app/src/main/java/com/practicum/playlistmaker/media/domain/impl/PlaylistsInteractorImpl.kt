package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.PlaylistsInteractor
import com.practicum.playlistmaker.media.domain.PlaylistsRepository
import com.practicum.playlistmaker.media.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) : PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistsRepository.addTrackToPlaylist(playlist, track)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return playlistsRepository.getPlaylistById(id)
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        playlistsRepository.removeTrackFromPlaylist(playlist, track)
    }

    override fun getTracksFromPlaylist(tracks: List<Int>): Flow<List<Track>> {
        return playlistsRepository.getTracksFromPlaylist(tracks)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsRepository.updatePlaylist(playlist)
    }
}