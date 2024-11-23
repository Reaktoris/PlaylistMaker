package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.model.Track

sealed class SearchState {
    data object Loading : SearchState()

    data class SearchContent(
        val tracks: MutableList<Track>
    ) : SearchState()

    data class HistoryContent(
        val tracks: MutableList<Track>
    ) : SearchState()

    data object ConnectionError : SearchState()

    data object EmptyResponseError : SearchState()

}