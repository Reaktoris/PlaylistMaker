package com.practicum.playlistmaker.media.ui.favorites

import com.practicum.playlistmaker.search.domain.model.Track

open class FavoritesState {
    data class Content(
        val tracks: List<Track>
    ) : FavoritesState()

    data object Empty : FavoritesState()
}