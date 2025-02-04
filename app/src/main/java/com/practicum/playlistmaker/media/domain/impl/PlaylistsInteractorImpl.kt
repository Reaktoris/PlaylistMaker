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
}