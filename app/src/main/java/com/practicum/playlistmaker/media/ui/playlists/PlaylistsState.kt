package com.practicum.playlistmaker.media.ui.playlists

import com.practicum.playlistmaker.media.domain.model.Playlist

open class PlaylistsState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState()
    data object Empty : PlaylistsState()
}