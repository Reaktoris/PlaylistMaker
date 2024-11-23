package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.model.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchState

class SearchViewModel : ViewModel() {
    private val trackInteractor = Creator.provideTrackInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val searchStateLiveData = MutableLiveData<SearchState>()

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
        trackInteractor.searchTracks(text, object: TrackInteractor.TrackConsumer<List<Track>?> {
            override fun consume(data: ConsumerData<List<Track>?>) {
                handler.post {
                    when(data) {
                        is ConsumerData.Data -> {
                            if (data.value!!.isNotEmpty()) {
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
        })
    }

}