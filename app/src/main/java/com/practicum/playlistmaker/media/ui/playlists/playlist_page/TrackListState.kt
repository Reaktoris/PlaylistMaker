package com.practicum.playlistmaker.media.ui.playlists.playlist_page

import com.practicum.playlistmaker.search.domain.model.Track

open class TrackListState {
    data class Content(
        val tracks: List<Track>
    ) : TrackListState()
    data object Empty : TrackListState()
}