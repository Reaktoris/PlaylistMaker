package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    private val searchStateLiveData = MutableLiveData<SearchState>()

    private var savedText: String? = null

    private var searchJob: Job? = null

    fun getSearchStateLiveData(): LiveData<SearchState> {
        return searchStateLiveData
    }

    fun saveTrack(track: Track) {
        searchHistoryInteractor.saveTrack(track)
    }

    fun getTrackList() : MutableList<Track> {
        return searchHistoryInteractor.getTrackList()
    }

    fun updateTrackList() {
        searchStateLiveData.value = SearchState.HistoryContent(searchHistoryInteractor.getTrackList())
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        updateTrackList()
    }
    fun searchTracks(text: String) {
        searchStateLiveData.value = SearchState.Loading
        viewModelScope.launch {
            trackInteractor.searchTracks(text).collect {data ->
                when(data) {
                    is ConsumerData.Data -> {
                        if (data.value.isNotEmpty()) {
                            searchStateLiveData.value = SearchState.SearchContent(data.value.toMutableList())
                        }
                        if (data.value.isEmpty()) {
                            searchStateLiveData.value = SearchState.EmptyResponseError
                        }
                    }
                    is ConsumerData.Error -> {
                        searchStateLiveData.value = SearchState.ConnectionError
                    }
                }
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (savedText == changedText) {
            return
        }
        savedText = changedText
        searchJob?.cancel()
        if (savedText != SEARCH_TEXT_DEF) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchTracks(changedText)
            }
        } else {
            updateTrackList()
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT_DEF =""
    }

}